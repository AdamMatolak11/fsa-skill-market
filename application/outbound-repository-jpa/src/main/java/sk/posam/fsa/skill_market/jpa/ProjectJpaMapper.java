package sk.posam.fsa.skill_market.jpa;

import org.springframework.stereotype.Component;
import sk.posam.fsa.skill_market.domain.project.Project;

@Component
public class ProjectJpaMapper {

    public Project toDomain(ProjectJpaEntity entity) {
        return Project.restore(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getBudget(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
