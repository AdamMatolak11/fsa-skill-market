package sk.posam.fsa.skill_market.domain.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import sk.posam.fsa.skill_market.domain.project.CreateProjectCommand;
import sk.posam.fsa.skill_market.domain.project.Project;
import sk.posam.fsa.skill_market.domain.project.ProjectAlreadyExistsException;
import sk.posam.fsa.skill_market.domain.project.ProjectCatalog;
import sk.posam.fsa.skill_market.domain.project.ProjectCommandRepository;
import sk.posam.fsa.skill_market.domain.project.ProjectNotFoundException;
import sk.posam.fsa.skill_market.domain.project.ProjectQueryRepository;
import sk.posam.fsa.skill_market.domain.project.UpdateProjectCommand;

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
                command.clientId(),
                command.title(),
                command.description(),
                command.budget(),
                OffsetDateTime.now()
        );
        return projectCommandRepository.save(project);
    }

    @Override
    public Project updateProject(UpdateProjectCommand command) {
        Project existingProject = projectQueryRepository.findById(command.projectId())
                .orElseThrow(() -> new ProjectNotFoundException(command.projectId()));

        return projectCommandRepository.save(existingProject.update(
                command.title(),
                command.description(),
                command.budget()
        ));
    }

    @Override
    public void cancelProject(UUID projectId) {
        Project existingProject = projectQueryRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        projectCommandRepository.save(existingProject.cancel());
    }
}
