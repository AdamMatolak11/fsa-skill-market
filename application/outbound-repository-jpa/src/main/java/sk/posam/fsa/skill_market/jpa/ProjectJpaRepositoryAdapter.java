package sk.posam.fsa.skill_market.jpa;

import java.util.List;
import org.springframework.stereotype.Repository;
import sk.posam.fsa.skill_market.domain.project.Project;
import sk.posam.fsa.skill_market.domain.project.ProjectQueryRepository;

@Repository
public class ProjectJpaRepositoryAdapter implements ProjectQueryRepository {

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
}
