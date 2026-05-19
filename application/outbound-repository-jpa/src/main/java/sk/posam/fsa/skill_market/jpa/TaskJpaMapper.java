package sk.posam.fsa.skill_market.jpa;

import org.springframework.stereotype.Component;
import sk.posam.fsa.skill_market.domain.task.Task;

@Component
public class TaskJpaMapper {

    public Task toDomain(TaskJpaEntity entity) {
        return Task.restore(
                entity.getId(),
                entity.getProjectId(),
                entity.getAssigneeUserId(),
                entity.getCreatedByUserId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public TaskJpaEntity toEntity(Task task) {
        TaskJpaEntity entity = new TaskJpaEntity();
        entity.setId(task.id());
        entity.setProjectId(task.projectId());
        entity.setAssigneeUserId(task.assigneeUserId());
        entity.setCreatedByUserId(task.createdByUserId());
        entity.setTitle(task.title());
        entity.setDescription(task.description());
        entity.setStatus(task.status().name());
        entity.setCreatedAt(task.createdAt());
        entity.setUpdatedAt(task.updatedAt());
        return entity;
    }
}
