package sk.posam.fsa.skill_market.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import sk.posam.fsa.skill_market.domain.service.ProjectFacade;
import sk.posam.fsa.skill_market.mapper.ProjectRestMapper;
import sk.posam.fsa.skill_market.rest.api.ProjectsApi;
import sk.posam.fsa.skill_market.rest.dto.CreateProjectRequest;
import sk.posam.fsa.skill_market.rest.dto.ProjectResponse;

@RestController
public class ProjectRestController implements ProjectsApi {

    private final ProjectFacade projectFacade;
    private final ProjectRestMapper projectRestMapper;

    public ProjectRestController(ProjectFacade projectFacade, ProjectRestMapper projectRestMapper) {
        this.projectFacade = projectFacade;
        this.projectRestMapper = projectRestMapper;
    }

    @Override
    public ResponseEntity<List<ProjectResponse>> getAllProjects() {
        List<ProjectResponse> body = projectFacade.getAllProjects().stream()
                .map(projectRestMapper::toResponse)
                .toList();
        return ResponseEntity.ok(body);
    }

    @Override
    public ResponseEntity<ProjectResponse> createProject(CreateProjectRequest createProjectRequest) {
        ProjectResponse response = projectRestMapper.toResponse(
                projectFacade.createProject(projectRestMapper.toCommand(createProjectRequest))
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
