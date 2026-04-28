package sk.posam.fsa.skill_market.domain.project;

import java.util.UUID;

public class ProjectNotFoundException extends RuntimeException {

    public ProjectNotFoundException(UUID projectId) {
        super("Project with id '" + projectId + "' was not found");
    }
}
