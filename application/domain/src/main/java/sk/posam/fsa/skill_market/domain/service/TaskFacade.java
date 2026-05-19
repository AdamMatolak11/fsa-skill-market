package sk.posam.fsa.skill_market.domain.service;

import java.util.List;
import java.util.UUID;
import sk.posam.fsa.skill_market.domain.project.Project;
import sk.posam.fsa.skill_market.domain.task.CreateTaskCommand;
import sk.posam.fsa.skill_market.domain.task.CreateTaskCommentCommand;
import sk.posam.fsa.skill_market.domain.task.Task;
import sk.posam.fsa.skill_market.domain.task.TaskComment;
import sk.posam.fsa.skill_market.domain.task.UpdateTaskCommand;

public interface TaskFacade {

    List<Project> getAssignedProjects(UUID freelancerId);

    List<Task> getProjectTasks(UUID projectId);

    Task createTask(CreateTaskCommand command);

    Task updateTask(UpdateTaskCommand command);

    List<TaskComment> getTaskComments(UUID projectId, UUID taskId);

    TaskComment createTaskComment(CreateTaskCommentCommand command);
}
