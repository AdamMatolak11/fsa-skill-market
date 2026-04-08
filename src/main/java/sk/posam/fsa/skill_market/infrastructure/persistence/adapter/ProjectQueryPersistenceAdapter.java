package sk.posam.fsa.skill_market.infrastructure.persistence.adapter;

import java.util.List;
import org.springframework.stereotype.Component;
import sk.posam.fsa.skill_market.domain.project.Project;
import sk.posam.fsa.skill_market.domain.project.ProjectQueryPort;
import sk.posam.fsa.skill_market.infrastructure.persistence.mapper.ProjectPersistenceMapper;
import sk.posam.fsa.skill_market.infrastructure.persistence.repository.ProjectJpaRepository;

@Component
public class ProjectQueryPersistenceAdapter implements ProjectQueryPort {

	private final ProjectJpaRepository projectJpaRepository;
	private final ProjectPersistenceMapper projectPersistenceMapper;

	public ProjectQueryPersistenceAdapter(
		ProjectJpaRepository projectJpaRepository,
		ProjectPersistenceMapper projectPersistenceMapper
	) {
		this.projectJpaRepository = projectJpaRepository;
		this.projectPersistenceMapper = projectPersistenceMapper;
	}

	@Override
	public List<Project> findAll() {
		return projectJpaRepository.findAll()
			.stream()
			.map(projectPersistenceMapper::toDomain)
			.toList();
	}
}
