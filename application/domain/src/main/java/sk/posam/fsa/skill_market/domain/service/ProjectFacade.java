package sk.posam.fsa.skill_market.domain.service;

import java.util.List;
import java.util.UUID;
import sk.posam.fsa.skill_market.domain.project.CreateProjectCommand;
import sk.posam.fsa.skill_market.domain.project.Project;
import sk.posam.fsa.skill_market.domain.project.UpdateProjectCommand;

public interface ProjectFacade {

    List<Project> getAllProjects();

    Project createProject(CreateProjectCommand command);

    Project updateProject(UpdateProjectCommand command);

    void cancelProject(UUID projectId);
}
