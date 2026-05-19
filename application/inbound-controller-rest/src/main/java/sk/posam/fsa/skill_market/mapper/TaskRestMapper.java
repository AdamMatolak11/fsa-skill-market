package sk.posam.fsa.skill_market.mapper;

import java.util.UUID;
import org.springframework.stereotype.Component;
import sk.posam.fsa.skill_market.domain.task.CreateTaskCommand;
import sk.posam.fsa.skill_market.domain.task.CreateTaskCommentCommand;
import sk.posam.fsa.skill_market.domain.task.Task;
import sk.posam.fsa.skill_market.domain.task.TaskComment;
import sk.posam.fsa.skill_market.domain.task.UpdateTaskCommand;
import sk.posam.fsa.skill_market.rest.dto.CreateTaskCommentRequest;
import sk.posam.fsa.skill_market.rest.dto.CreateTaskRequest;
import sk.posam.fsa.skill_market.rest.dto.TaskCommentResponse;
import sk.posam.fsa.skill_market.rest.dto.TaskResponse;
import sk.posam.fsa.skill_market.rest.dto.UpdateTaskRequest;

@Component
public class TaskRestMapper {

    public TaskResponse toResponse(Task task) {
        TaskResponse response = new TaskResponse();
        response.setId(task.id());
        response.setProjectId(task.projectId());
        response.setAssigneeUserId(task.assigneeUserId());
        response.setCreatedByUserId(task.createdByUserId());
        response.setTitle(task.title());
        response.setDescription(task.description());
        response.setStatus(TaskResponse.StatusEnum.fromValue(task.status().name()));
        response.setCreatedAt(task.createdAt());
        response.setUpdatedAt(task.updatedAt());
        return response;
    }

    public TaskCommentResponse toResponse(TaskComment taskComment) {
        TaskCommentResponse response = new TaskCommentResponse();
        response.setId(taskComment.id());
        response.setTaskId(taskComment.taskId());
        response.setAuthorUserId(taskComment.authorUserId());
        response.setMessage(taskComment.message());
        response.setCreatedAt(taskComment.createdAt());
        return response;
    }

    public CreateTaskCommand toCommand(UUID projectId, CreateTaskRequest request) {
        return new CreateTaskCommand(
                projectId,
                request.getCreatorUserId(),
                request.getAssigneeUserId(),
                request.getTitle(),
                request.getDescription()
        );
    }

    public UpdateTaskCommand toCommand(UUID projectId, UUID taskId, UpdateTaskRequest request) {
        return new UpdateTaskCommand(
                projectId,
                taskId,
                request.getAssigneeUserId(),
                request.getTitle(),
                request.getDescription(),
                request.getStatus().getValue()
        );
    }

    public CreateTaskCommentCommand toCommand(UUID projectId, UUID taskId, CreateTaskCommentRequest request) {
        return new CreateTaskCommentCommand(
                projectId,
                taskId,
                request.getAuthorUserId(),
                request.getMessage()
        );
    }
}
