package sk.posam.fsa.skill_market.application.project;

import java.util.List;
import sk.posam.fsa.skill_market.domain.project.Project;

public interface GetAllProjectsUseCase {

	List<Project> getAllProjects();
}
