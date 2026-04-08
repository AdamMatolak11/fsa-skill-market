package sk.posam.fsa.skill_market.application.project;

import java.util.List;
import org.springframework.stereotype.Service;
import sk.posam.fsa.skill_market.domain.project.Project;
import sk.posam.fsa.skill_market.domain.project.ProjectQueryPort;

@Service
public class GetAllProjectsService implements GetAllProjectsUseCase {

	private final ProjectQueryPort projectQueryPort;

	public GetAllProjectsService(ProjectQueryPort projectQueryPort) {
		this.projectQueryPort = projectQueryPort;
	}

	@Override
	public List<Project> getAllProjects() {
		return projectQueryPort.findAll();
	}
}
