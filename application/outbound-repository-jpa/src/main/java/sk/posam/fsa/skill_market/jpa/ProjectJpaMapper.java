package sk.posam.fsa.skill_market.jpa;

import org.springframework.stereotype.Component;
import sk.posam.fsa.skill_market.domain.project.Project;

@Component
public class ProjectJpaMapper {

    public Project toDomain(ProjectJpaEntity entity) {
        return Project.restore(
                entity.getId(),
                entity.getClientId(),
                entity.getAssignedFreelancerId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getBudget(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }

    public ProjectJpaEntity toEntity(Project project) {
        ProjectJpaEntity entity = new ProjectJpaEntity();
        entity.setId(project.id());
        entity.setClientId(project.clientId());
        entity.setAssignedFreelancerId(project.assignedFreelancerId());
        entity.setTitle(project.title());
        entity.setDescription(project.description());
        entity.setBudget(project.budget());
        entity.setStatus(project.status().name());
        entity.setCreatedAt(project.createdAt());
        return entity;
    }
}
