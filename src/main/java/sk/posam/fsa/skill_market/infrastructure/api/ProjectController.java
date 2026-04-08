package sk.posam.fsa.skill_market.infrastructure.api;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import sk.posam.fsa.skill_market.application.project.GetAllProjectsUseCase;
import sk.posam.fsa.skill_market.infrastructure.api.generated.api.ProjectsApi;
import sk.posam.fsa.skill_market.infrastructure.api.generated.model.ProjectResponse;
import sk.posam.fsa.skill_market.infrastructure.api.mapper.ProjectApiMapper;

@RestController
public class ProjectController implements ProjectsApi {

	private final GetAllProjectsUseCase getAllProjectsUseCase;
	private final ProjectApiMapper projectApiMapper;

	public ProjectController(
		GetAllProjectsUseCase getAllProjectsUseCase,
		ProjectApiMapper projectApiMapper
	) {
		this.getAllProjectsUseCase = getAllProjectsUseCase;
		this.projectApiMapper = projectApiMapper;
	}

	@Override
	public ResponseEntity<List<ProjectResponse>> getAllProjects() {
		List<ProjectResponse> body = getAllProjectsUseCase.getAllProjects()
			.stream()
			.map(projectApiMapper::toResponse)
			.toList();
		return ResponseEntity.ok(body);
	}
}
