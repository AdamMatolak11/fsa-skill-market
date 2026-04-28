package sk.posam.fsa.skill_market.domain.project;

import java.util.UUID;

public class ProjectNotAcceptingOffersException extends RuntimeException {

    public ProjectNotAcceptingOffersException(UUID projectId, ProjectStatus status) {
        super("Project with id '" + projectId + "' is in status '" + status + "' and does not accept offers");
    }
}
