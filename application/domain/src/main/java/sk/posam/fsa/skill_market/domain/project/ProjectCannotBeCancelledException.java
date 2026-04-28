package sk.posam.fsa.skill_market.domain.project;

import java.util.UUID;

public class ProjectCannotBeCancelledException extends RuntimeException {

    public ProjectCannotBeCancelledException(UUID projectId, ProjectStatus status) {
        super("Project with id '" + projectId + "' is in status '" + status + "' and cannot be cancelled");
    }
}
