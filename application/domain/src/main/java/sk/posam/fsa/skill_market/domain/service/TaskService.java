package sk.posam.fsa.skill_market.domain.service;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
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
import sk.posam.fsa.skill_market.domain.task.TaskNotFoundException;
import sk.posam.fsa.skill_market.domain.task.TaskParticipantMismatchException;
import sk.posam.fsa.skill_market.domain.task.TaskQueryRepository;
import sk.posam.fsa.skill_market.domain.task.TaskStatus;
import sk.posam.fsa.skill_market.domain.task.UpdateTaskCommand;

public class TaskService implements TaskFacade {

    private final ProjectQueryRepository projectQueryRepository;
    private final TaskQueryRepository taskQueryRepository;
    private final TaskCommandRepository taskCommandRepository;
    private final TaskCommentQueryRepository taskCommentQueryRepository;
    private final TaskCommentCommandRepository taskCommentCommandRepository;

    public TaskService(
            ProjectQueryRepository projectQueryRepository,
            TaskQueryRepository taskQueryRepository,
            TaskCommandRepository taskCommandRepository,
            TaskCommentQueryRepository taskCommentQueryRepository,
            TaskCommentCommandRepository taskCommentCommandRepository
    ) {
        this.projectQueryRepository = projectQueryRepository;
        this.taskQueryRepository = taskQueryRepository;
        this.taskCommandRepository = taskCommandRepository;
        this.taskCommentQueryRepository = taskCommentQueryRepository;
        this.taskCommentCommandRepository = taskCommentCommandRepository;
    }

    @Override
    public List<Project> getAssignedProjects(UUID freelancerId) {
        return projectQueryRepository.findByAssignedFreelancerId(freelancerId).stream()
                .sorted(Comparator.comparing(Project::createdAt).reversed())
                .toList();
    }

    @Override
    public List<Task> getProjectTasks(UUID projectId) {
        ensureTaskManagementAvailable(loadProject(projectId));
        return taskQueryRepository.findByProjectId(projectId).stream()
                .sorted(Comparator.comparing(Task::createdAt))
                .toList();
    }

    @Override
    public Task createTask(CreateTaskCommand command) {
        Project project = loadProject(command.projectId());
        ensureTaskManagementAvailable(project);
        ensureParticipant(project, command.creatorUserId());
        ensureAssignee(project, command.assigneeUserId());

        return taskCommandRepository.save(Task.createNew(
                command.projectId(),
                command.assigneeUserId(),
                command.creatorUserId(),
                command.title(),
                command.description(),
                OffsetDateTime.now()
        ));
    }

    @Override
    public Task updateTask(UpdateTaskCommand command) {
        Project project = loadProject(command.projectId());
        ensureTaskManagementAvailable(project);
        ensureAssignee(project, command.assigneeUserId());

        Task existingTask = loadTask(command.projectId(), command.taskId());
        return taskCommandRepository.save(existingTask.update(
                command.title(),
                command.description(),
                TaskStatus.from(command.status()),
                command.assigneeUserId(),
                OffsetDateTime.now()
        ));
    }

    @Override
    public List<TaskComment> getTaskComments(UUID projectId, UUID taskId) {
        Project project = loadProject(projectId);
        ensureTaskManagementAvailable(project);
        loadTask(projectId, taskId);
        return taskCommentQueryRepository.findByTaskId(taskId).stream()
                .sorted(Comparator.comparing(TaskComment::createdAt))
                .toList();
    }

    @Override
    public TaskComment createTaskComment(CreateTaskCommentCommand command) {
        Project project = loadProject(command.projectId());
        ensureTaskManagementAvailable(project);
        ensureParticipant(project, command.authorUserId());
        loadTask(command.projectId(), command.taskId());

        return taskCommentCommandRepository.save(TaskComment.createNew(
                command.taskId(),
                command.authorUserId(),
                command.message(),
                OffsetDateTime.now()
        ));
    }

    private Project loadProject(UUID projectId) {
        return projectQueryRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
    }

    private Task loadTask(UUID projectId, UUID taskId) {
        Task task = taskQueryRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
        if (!task.projectId().equals(projectId)) {
            throw new TaskNotFoundException(taskId);
        }
        return task;
    }

    private void ensureTaskManagementAvailable(Project project) {
        if (!project.hasAssignedFreelancer()) {
            throw new ProjectTaskManagementUnavailableException(project.id());
        }
    }

    private void ensureParticipant(Project project, UUID userId) {
        if (!project.hasParticipant(userId)) {
            throw new TaskParticipantMismatchException(project.id(), userId);
        }
    }

    private void ensureAssignee(Project project, UUID assigneeUserId) {
        if (assigneeUserId != null) {
            ensureParticipant(project, assigneeUserId);
        }
    }
}
