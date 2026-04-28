package sk.posam.fsa.skill_market.mapper;

import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.stereotype.Component;
import sk.posam.fsa.skill_market.domain.project.CreateProjectCommand;
import sk.posam.fsa.skill_market.domain.project.Project;
import sk.posam.fsa.skill_market.domain.project.UpdateProjectCommand;
import sk.posam.fsa.skill_market.rest.dto.CreateProjectRequest;
import sk.posam.fsa.skill_market.rest.dto.ProjectResponse;
import sk.posam.fsa.skill_market.rest.dto.UpdateProjectRequest;

@Component
public class ProjectRestMapper {

    public ProjectResponse toResponse(Project project) {
        ProjectResponse response = new ProjectResponse();
        response.setId(project.id());
        response.setClientId(project.clientId());
        response.setAssignedFreelancerId(project.assignedFreelancerId());
        response.setTitle(project.title());
        response.setDescription(project.description());
        response.setBudget(project.budget().doubleValue());
        response.setStatus(ProjectResponse.StatusEnum.fromValue(project.status().name()));
        response.setCreatedAt(project.createdAt());
        return response;
    }

    public CreateProjectCommand toCommand(UUID clientId, CreateProjectRequest request) {
        return new CreateProjectCommand(
                clientId,
                request.getTitle(),
                request.getDescription(),
                BigDecimal.valueOf(request.getBudget())
        );
    }

    public UpdateProjectCommand toCommand(UUID projectId, UpdateProjectRequest request) {
        return new UpdateProjectCommand(
                projectId,
                request.getTitle(),
                request.getDescription(),
                BigDecimal.valueOf(request.getBudget())
        );
    }
}
