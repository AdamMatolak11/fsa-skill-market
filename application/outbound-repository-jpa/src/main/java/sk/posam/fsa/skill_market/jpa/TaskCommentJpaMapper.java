package sk.posam.fsa.skill_market.jpa;

import org.springframework.stereotype.Component;
import sk.posam.fsa.skill_market.domain.task.TaskComment;

@Component
public class TaskCommentJpaMapper {

    public TaskComment toDomain(TaskCommentJpaEntity entity) {
        return TaskComment.restore(
                entity.getId(),
                entity.getTaskId(),
                entity.getAuthorUserId(),
                entity.getMessage(),
                entity.getCreatedAt()
        );
    }

    public TaskCommentJpaEntity toEntity(TaskComment taskComment) {
        TaskCommentJpaEntity entity = new TaskCommentJpaEntity();
        entity.setId(taskComment.id());
        entity.setTaskId(taskComment.taskId());
        entity.setAuthorUserId(taskComment.authorUserId());
        entity.setMessage(taskComment.message());
        entity.setCreatedAt(taskComment.createdAt());
        return entity;
    }
}
