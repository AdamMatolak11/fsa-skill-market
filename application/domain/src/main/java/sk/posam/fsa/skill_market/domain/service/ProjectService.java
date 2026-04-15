package sk.posam.fsa.skill_market.domain.service;

import java.util.List;
import sk.posam.fsa.skill_market.domain.project.Project;
import sk.posam.fsa.skill_market.domain.project.ProjectCatalog;
import sk.posam.fsa.skill_market.domain.project.ProjectQueryRepository;

public class ProjectService implements ProjectFacade {

    private final ProjectQueryRepository projectQueryRepository;

    public ProjectService(ProjectQueryRepository projectQueryRepository) {
        this.projectQueryRepository = projectQueryRepository;
    }

    @Override
    public List<Project> getAllProjects() {
        return ProjectCatalog.of(projectQueryRepository.findAll())
                .projectsVisibleInMarketplace();
    }
}
