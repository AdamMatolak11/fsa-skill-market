package sk.posam.fsa.skill_market.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;
import sk.posam.fsa.skill_market.domain.project.Project;
import sk.posam.fsa.skill_market.infrastructure.persistence.entity.ProjectJpaEntity;

@Component
public class ProjectPersistenceMapper {

	public Project toDomain(ProjectJpaEntity entity) {
		return new Project(
			entity.getId(),
			entity.getTitle(),
			entity.getDescription(),
			entity.getBudget(),
			entity.getStatus(),
			entity.getCreatedAt()
		);
	}
}
