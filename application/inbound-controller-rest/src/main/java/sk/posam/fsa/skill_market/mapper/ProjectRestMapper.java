package sk.posam.fsa.skill_market.mapper;

import org.springframework.stereotype.Component;
import sk.posam.fsa.skill_market.domain.project.Project;
import sk.posam.fsa.skill_market.rest.dto.ProjectResponse;

@Component
public class ProjectRestMapper {

    public ProjectResponse toResponse(Project project) {
        ProjectResponse response = new ProjectResponse();
        response.setId(project.id());
        response.setTitle(project.title());
        response.setDescription(project.description());
        response.setBudget(project.budget().doubleValue());
        response.setStatus(ProjectResponse.StatusEnum.fromValue(project.status().name()));
        response.setCreatedAt(project.createdAt());
        return response;
    }
}
