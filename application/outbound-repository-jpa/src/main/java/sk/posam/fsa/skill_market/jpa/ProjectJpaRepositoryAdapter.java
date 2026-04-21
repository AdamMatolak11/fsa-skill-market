package sk.posam.fsa.skill_market.jpa;

import java.util.List;
import org.springframework.stereotype.Repository;
import sk.posam.fsa.skill_market.domain.project.Project;
import sk.posam.fsa.skill_market.domain.project.ProjectCommandRepository;
import sk.posam.fsa.skill_market.domain.project.ProjectQueryRepository;

@Repository
public class ProjectJpaRepositoryAdapter implements ProjectQueryRepository, ProjectCommandRepository {

    private final ProjectSpringDataRepository projectSpringDataRepository;
    private final ProjectJpaMapper projectJpaMapper;

    public ProjectJpaRepositoryAdapter(
            ProjectSpringDataRepository projectSpringDataRepository,
            ProjectJpaMapper projectJpaMapper
    ) {
        this.projectSpringDataRepository = projectSpringDataRepository;
        this.projectJpaMapper = projectJpaMapper;
    }

    @Override
    public List<Project> findAll() {
        return projectSpringDataRepository.findAll().stream()
                .map(projectJpaMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByTitle(String title) {
        return projectSpringDataRepository.existsByTitleIgnoreCase(title.trim());
    }

    @Override
    public Project save(Project project) {
        return projectJpaMapper.toDomain(projectSpringDataRepository.save(projectJpaMapper.toEntity(project)));
    }
}
