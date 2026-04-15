package sk.posam.fsa.skill_market.domain.service;

import java.util.List;
import java.time.OffsetDateTime;
import sk.posam.fsa.skill_market.domain.project.CreateProjectCommand;
import sk.posam.fsa.skill_market.domain.project.Project;
import sk.posam.fsa.skill_market.domain.project.ProjectAlreadyExistsException;
import sk.posam.fsa.skill_market.domain.project.ProjectCatalog;
import sk.posam.fsa.skill_market.domain.project.ProjectCommandRepository;
import sk.posam.fsa.skill_market.domain.project.ProjectQueryRepository;

public class ProjectService implements ProjectFacade {

    private final ProjectQueryRepository projectQueryRepository;
    private final ProjectCommandRepository projectCommandRepository;

    public ProjectService(
            ProjectQueryRepository projectQueryRepository,
            ProjectCommandRepository projectCommandRepository
    ) {
        this.projectQueryRepository = projectQueryRepository;
        this.projectCommandRepository = projectCommandRepository;
    }

    @Override
    public List<Project> getAllProjects() {
        return ProjectCatalog.of(projectQueryRepository.findAll())
                .projectsVisibleInMarketplace();
    }

    @Override
    public Project createProject(CreateProjectCommand command) {
        if (projectCommandRepository.existsByTitle(command.title())) {
            throw new ProjectAlreadyExistsException(command.title());
        }

        Project project = Project.createNew(
                command.title(),
                command.description(),
                command.budget(),
                OffsetDateTime.now()
        );
        return projectCommandRepository.save(project);
    }
}
