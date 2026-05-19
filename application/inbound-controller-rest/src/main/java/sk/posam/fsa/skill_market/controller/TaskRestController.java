package sk.posam.fsa.skill_market.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import sk.posam.fsa.skill_market.domain.project.Project;
import sk.posam.fsa.skill_market.domain.project.ProjectNotFoundException;
import sk.posam.fsa.skill_market.domain.project.ProjectQueryRepository;
import sk.posam.fsa.skill_market.domain.service.TaskFacade;
import sk.posam.fsa.skill_market.mapper.TaskRestMapper;
import sk.posam.fsa.skill_market.rest.api.TasksApi;
import sk.posam.fsa.skill_market.rest.dto.CreateTaskCommentRequest;
import sk.posam.fsa.skill_market.rest.dto.CreateTaskRequest;
import sk.posam.fsa.skill_market.rest.dto.TaskCommentResponse;
import sk.posam.fsa.skill_market.rest.dto.TaskResponse;
import sk.posam.fsa.skill_market.rest.dto.UpdateTaskRequest;
import sk.posam.fsa.skill_market.security.AuthenticatedUser;
import sk.posam.fsa.skill_market.security.AuthenticatedUserProvider;
import sk.posam.fsa.skill_market.security.ForbiddenOperationException;

@RestController
public class TaskRestController implements TasksApi {

    private final TaskFacade taskFacade;
    private final TaskRestMapper taskRestMapper;
    private final ProjectQueryRepository projectQueryRepository;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    public TaskRestController(
            TaskFacade taskFacade,
            TaskRestMapper taskRestMapper,
            ProjectQueryRepository projectQueryRepository,
            AuthenticatedUserProvider authenticatedUserProvider
    ) {
        this.taskFacade = taskFacade;
        this.taskRestMapper = taskRestMapper;
        this.projectQueryRepository = projectQueryRepository;
        this.authenticatedUserProvider = authenticatedUserProvider;
    }

    @Override
    public ResponseEntity<List<TaskResponse>> getProjectTasks(UUID projectId) {
        assertCanAccessProjectWorkspace(projectId);
        return ResponseEntity.ok(taskFacade.getProjectTasks(projectId).stream()
                .map(taskRestMapper::toResponse)
                .toList());
    }

    @Override
    public ResponseEntity<TaskResponse> createProjectTask(UUID projectId, CreateTaskRequest createTaskRequest) {
        assertCanAccessProjectWorkspace(projectId);
        assertCanActAs(createTaskRequest.getCreatorUserId(), "create task");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(taskRestMapper.toResponse(taskFacade.createTask(taskRestMapper.toCommand(projectId, createTaskRequest))));
    }

    @Override
    public ResponseEntity<TaskResponse> updateProjectTask(UUID projectId, UUID taskId, UpdateTaskRequest updateTaskRequest) {
        assertCanAccessProjectWorkspace(projectId);
        return ResponseEntity.ok(taskRestMapper.toResponse(
                taskFacade.updateTask(taskRestMapper.toCommand(projectId, taskId, updateTaskRequest))
        ));
    }

    @Override
    public ResponseEntity<List<TaskCommentResponse>> getTaskComments(UUID projectId, UUID taskId) {
        assertCanAccessProjectWorkspace(projectId);
        return ResponseEntity.ok(taskFacade.getTaskComments(projectId, taskId).stream()
                .map(taskRestMapper::toResponse)
                .toList());
    }

    @Override
    public ResponseEntity<TaskCommentResponse> createTaskComment(
            UUID projectId,
            UUID taskId,
            CreateTaskCommentRequest createTaskCommentRequest
    ) {
        assertCanAccessProjectWorkspace(projectId);
        assertCanActAs(createTaskCommentRequest.getAuthorUserId(), "comment on task");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(taskRestMapper.toResponse(
                        taskFacade.createTaskComment(taskRestMapper.toCommand(projectId, taskId, createTaskCommentRequest))
                ));
    }

    private void assertCanAccessProjectWorkspace(UUID projectId) {
        Project project = projectQueryRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        Optional<AuthenticatedUser> currentUser = authenticatedUserProvider.currentUser();
        if (currentUser.isEmpty()) {
            return;
        }

        AuthenticatedUser user = currentUser.get();
        if (user.hasRole("ADMIN")) {
            return;
        }
        if (!project.hasParticipant(user.userId())) {
            throw new ForbiddenOperationException("Current user cannot access workspace for project '" + projectId + "'");
        }
    }

    private void assertCanActAs(UUID userId, String action) {
        Optional<AuthenticatedUser> currentUser = authenticatedUserProvider.currentUser();
        if (currentUser.isEmpty()) {
            return;
        }

        AuthenticatedUser user = currentUser.get();
        if (!user.hasRole("ADMIN") && !user.userId().equals(userId)) {
            throw new ForbiddenOperationException("Current user cannot " + action + " as user '" + userId + "'");
        }
    }
}
