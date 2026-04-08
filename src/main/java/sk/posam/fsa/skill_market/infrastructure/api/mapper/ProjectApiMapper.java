package sk.posam.fsa.skill_market.infrastructure.api.mapper;

import org.springframework.stereotype.Component;
import sk.posam.fsa.skill_market.domain.project.Project;
import sk.posam.fsa.skill_market.infrastructure.api.generated.model.ProjectResponse;

@Component
public class ProjectApiMapper {

	public ProjectResponse toResponse(Project project) {
		ProjectResponse response = new ProjectResponse();
		response.setId(project.id());
		response.setTitle(project.title());
		response.setDescription(project.description());
		response.setBudget(project.budget().doubleValue());
		response.setStatus(ProjectResponse.StatusEnum.fromValue(project.status()));
		response.setCreatedAt(project.createdAt());
		return response;
	}
}
