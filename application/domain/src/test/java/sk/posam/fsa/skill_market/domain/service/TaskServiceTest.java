package sk.posam.fsa.skill_market.domain.service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import sk.posam.fsa.skill_market.domain.project.Project;
import sk.posam.fsa.skill_market.domain.project.ProjectNotFoundException;
import sk.posam.fsa.skill_market.domain.project.ProjectQueryRepository;
import sk.posam.fsa.skill_market.domain.task.CreateTaskCommand;
import sk.posam.fsa.skill_market.domain.task.CreateTaskCommentCommand;
import sk.posam.fsa.skill_market.domain.task.ProjectTaskManagementUnavailableException;
import sk.posam.fsa.skill_market.domain.task.Task;
import sk.posam.fsa.skill_market.domain.task.TaskCommandRepository;
import sk.posam.fsa.skill_market.domain.task.TaskComment;
import sk.posam.fsa.skill_market.domain.task.TaskCommentCommandRepository;
import sk.posam.fsa.skill_market.domain.task.TaskCommentQueryRepository;
import sk.posam.fsa.skill_market.domain.task.TaskParticipantMismatchException;
import sk.posam.fsa.skill_market.domain.task.TaskQueryRepository;
import sk.posam.fsa.skill_market.domain.task.UpdateTaskCommand;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskServiceTest {

    @Test
    void createTask_persistsTaskForAssignedProject() {
        Project project = assignedProject();
        InMemoryTaskRepository taskRepository = new InMemoryTaskRepository();
        TaskService service = new TaskService(
                new SingleProjectQueryRepository(project),
                taskRepository,
                taskRepository,
                new InMemoryTaskCommentRepository(),
                new InMemoryTaskCommentRepository()
        );

        Task createdTask = service.createTask(new CreateTaskCommand(
                project.id(),
                project.clientId(),
                project.assignedFreelancerId(),
                "Prepare board",
                "Create the initial project board setup."
        ));

        assertEquals("TODO", createdTask.status().name());
        assertEquals(project.assignedFreelancerId(), createdTask.assigneeUserId());
        assertTrue(taskRepository.savedTask != null);
    }

    @Test
    void createTask_rejectsProjectsWithoutAssignedFreelancer() {
        Project project = openProject();
        TaskService service = new TaskService(
                new SingleProjectQueryRepository(project),
                new InMemoryTaskRepository(),
                new InMemoryTaskRepository(),
                new InMemoryTaskCommentRepository(),
                new InMemoryTaskCommentRepository()
        );

        assertThrows(ProjectTaskManagementUnavailableException.class, () -> service.createTask(new CreateTaskCommand(
                project.id(),
                project.clientId(),
                null,
                "Task title",
                "Task description"
        )));
    }

    @Test
    void createTask_rejectsAssigneeOutsideProjectParticipants() {
        Project project = assignedProject();
        TaskService service = new TaskService(
                new SingleProjectQueryRepository(project),
                new InMemoryTaskRepository(),
                new InMemoryTaskRepository(),
                new InMemoryTaskCommentRepository(),
                new InMemoryTaskCommentRepository()
        );

        assertThrows(TaskParticipantMismatchException.class, () -> service.createTask(new CreateTaskCommand(
                project.id(),
                project.clientId(),
                UUID.randomUUID(),
                "Task title",
                "Task description"
        )));
    }

    @Test
    void createTaskComment_persistsParticipantComment() {
        Project project = assignedProject();
        InMemoryTaskRepository taskRepository = new InMemoryTaskRepository();
        Task task = Task.createNew(
                project.id(),
                project.assignedFreelancerId(),
                project.clientId(),
                "API design",
                "Define task DTOs",
                OffsetDateTime.parse("2026-03-02T09:00:00+01:00")
        );
        taskRepository.tasks.add(task);
        InMemoryTaskCommentRepository taskCommentRepository = new InMemoryTaskCommentRepository();
        TaskService service = new TaskService(
                new SingleProjectQueryRepository(project),
                taskRepository,
                taskRepository,
                taskCommentRepository,
                taskCommentRepository
        );

        TaskComment comment = service.createTaskComment(new CreateTaskCommentCommand(
                project.id(),
                task.id(),
                project.assignedFreelancerId(),
                "I will post the first draft today."
        ));

        assertEquals(task.id(), comment.taskId());
        assertTrue(taskCommentRepository.savedComment != null);
    }

    @Test
    void getProjectTasks_returnsNotFoundForUnknownProject() {
        TaskService service = new TaskService(
                new EmptyProjectQueryRepository(),
                new InMemoryTaskRepository(),
                new InMemoryTaskRepository(),
                new InMemoryTaskCommentRepository(),
                new InMemoryTaskCommentRepository()
        );

        assertThrows(ProjectNotFoundException.class, () -> service.getProjectTasks(UUID.randomUUID()));
    }

    @Test
    void updateTask_updatesStatusAndAssignee() {
        Project project = assignedProject();
        InMemoryTaskRepository taskRepository = new InMemoryTaskRepository();
        Task existingTask = Task.createNew(
                project.id(),
                project.assignedFreelancerId(),
                project.clientId(),
                "API design",
                "Define task DTOs",
                OffsetDateTime.parse("2026-03-02T09:00:00+01:00")
        );
        taskRepository.tasks.add(existingTask);
        TaskService service = new TaskService(
                new SingleProjectQueryRepository(project),
                taskRepository,
                taskRepository,
                new InMemoryTaskCommentRepository(),
                new InMemoryTaskCommentRepository()
        );

        Task updatedTask = service.updateTask(new UpdateTaskCommand(
                project.id(),
                existingTask.id(),
                project.clientId(),
                "API design ready",
                "DTOs and endpoints defined.",
                "IN_REVIEW"
        ));

        assertEquals("IN_REVIEW", updatedTask.status().name());
        assertEquals(project.clientId(), updatedTask.assigneeUserId());
        assertEquals("API design ready", updatedTask.title());
    }

    private static Project assignedProject() {
        return Project.restore(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Assigned project",
                "Project with freelancer",
                BigDecimal.valueOf(2000),
                "IN_PROGRESS",
                OffsetDateTime.parse("2026-03-01T10:00:00+01:00")
        );
    }

    private static Project openProject() {
        return Project.restore(
                UUID.randomUUID(),
                UUID.randomUUID(),
                null,
                "Open project",
                "Project without freelancer",
                BigDecimal.valueOf(1500),
                "OPEN",
                OffsetDateTime.parse("2026-02-01T10:00:00+01:00")
        );
    }

    private static final class SingleProjectQueryRepository implements ProjectQueryRepository {
        private final Project project;

        private SingleProjectQueryRepository(Project project) {
            this.project = project;
        }

        @Override
        public List<Project> findAll() {
            return List.of(project);
        }

        @Override
        public List<Project> findByAssignedFreelancerId(UUID freelancerId) {
            return project.assignedFreelancerId() != null && project.assignedFreelancerId().equals(freelancerId)
                    ? List.of(project)
                    : List.of();
        }

        @Override
        public List<Project> findByClientId(UUID clientId) {
            return project.clientId() != null && project.clientId().equals(clientId)
                    ? List.of(project)
                    : List.of();
        }

        @Override
        public Optional<Project> findById(UUID projectId) {
            return project.id().equals(projectId) ? Optional.of(project) : Optional.empty();
        }
    }

    private static final class EmptyProjectQueryRepository implements ProjectQueryRepository {
        @Override
        public List<Project> findAll() {
            return List.of();
        }

        @Override
        public List<Project> findByAssignedFreelancerId(UUID freelancerId) {
            return List.of();
        }

        @Override
        public List<Project> findByClientId(UUID clientId) {
            return List.of();
        }

        @Override
        public Optional<Project> findById(UUID projectId) {
            return Optional.empty();
        }
    }

    private static final class InMemoryTaskRepository implements TaskQueryRepository, TaskCommandRepository {
        private final List<Task> tasks = new ArrayList<>();
        private Task savedTask;

        @Override
        public List<Task> findByProjectId(UUID projectId) {
            return tasks.stream()
                    .filter(task -> task.projectId().equals(projectId))
                    .toList();
        }

        @Override
        public Optional<Task> findById(UUID taskId) {
            return tasks.stream()
                    .filter(task -> task.id().equals(taskId))
                    .findFirst();
        }

        @Override
        public Task save(Task task) {
            tasks.removeIf(existingTask -> existingTask.id().equals(task.id()));
            tasks.add(task);
            this.savedTask = task;
            return task;
        }
    }

    private static final class InMemoryTaskCommentRepository implements TaskCommentQueryRepository, TaskCommentCommandRepository {
        private final List<TaskComment> comments = new ArrayList<>();
        private TaskComment savedComment;

        @Override
        public List<TaskComment> findByTaskId(UUID taskId) {
            return comments.stream()
                    .filter(comment -> comment.taskId().equals(taskId))
                    .toList();
        }

        @Override
        public TaskComment save(TaskComment taskComment) {
            comments.add(taskComment);
            this.savedComment = taskComment;
            return taskComment;
        }
    }
}
