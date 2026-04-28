package sk.posam.fsa.skill_market.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import sk.posam.fsa.skill_market.domain.project.Project;
import sk.posam.fsa.skill_market.domain.project.ProjectNotFoundException;
import sk.posam.fsa.skill_market.domain.project.ProjectQueryRepository;
import sk.posam.fsa.skill_market.domain.service.ProjectFacade;
import sk.posam.fsa.skill_market.mapper.ProjectRestMapper;
import sk.posam.fsa.skill_market.rest.api.ProjectsApi;
import sk.posam.fsa.skill_market.rest.dto.CreateProjectRequest;
import sk.posam.fsa.skill_market.rest.dto.ProjectResponse;
import sk.posam.fsa.skill_market.rest.dto.UpdateProjectRequest;
import sk.posam.fsa.skill_market.security.AuthenticatedUser;
import sk.posam.fsa.skill_market.security.AuthenticatedUserProvider;
import sk.posam.fsa.skill_market.security.ForbiddenOperationException;

@RestController
public class ProjectRestController implements ProjectsApi {

    private final ProjectFacade projectFacade;
    private final ProjectRestMapper projectRestMapper;
    private final ProjectQueryRepository projectQueryRepository;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    public ProjectRestController(
            ProjectFacade projectFacade,
            ProjectRestMapper projectRestMapper,
            ProjectQueryRepository projectQueryRepository,
            AuthenticatedUserProvider authenticatedUserProvider
    ) {
        this.projectFacade = projectFacade;
        this.projectRestMapper = projectRestMapper;
        this.projectQueryRepository = projectQueryRepository;
        this.authenticatedUserProvider = authenticatedUserProvider;
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
        UUID clientId = authenticatedUserProvider.currentUser()
                .map(AuthenticatedUser::userId)
                .orElse(null);
        ProjectResponse response = projectRestMapper.toResponse(
                projectFacade.createProject(projectRestMapper.toCommand(clientId, createProjectRequest))
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<ProjectResponse> updateProject(UUID projectId, UpdateProjectRequest updateProjectRequest) {
        assertCanManageProject(projectId);
        ProjectResponse response = projectRestMapper.toResponse(
                projectFacade.updateProject(projectRestMapper.toCommand(projectId, updateProjectRequest))
        );
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> cancelProject(UUID projectId) {
        assertCanManageProject(projectId);
        projectFacade.cancelProject(projectId);
        return ResponseEntity.noContent().build();
    }

    private void assertCanManageProject(UUID projectId) {
        Project project = projectQueryRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        Optional<AuthenticatedUser> currentUser = authenticatedUserProvider.currentUser();
        if (currentUser.isEmpty()) {
            return;
        }

        AuthenticatedUser user = currentUser.get();
        if (user.hasRole("ADMIN")) {
            return;
        }
        if (project.clientId() != null && !project.clientId().equals(user.userId())) {
            throw new ForbiddenOperationException("Current user cannot manage project '" + projectId + "'");
        }
    }
}
