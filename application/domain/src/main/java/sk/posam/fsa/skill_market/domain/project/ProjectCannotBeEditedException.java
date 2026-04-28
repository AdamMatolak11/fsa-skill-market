package sk.posam.fsa.skill_market.domain.project;

import java.util.UUID;

public class ProjectCannotBeEditedException extends RuntimeException {

    public ProjectCannotBeEditedException(UUID projectId, ProjectStatus status) {
        super("Project with id '" + projectId + "' is in status '" + status + "' and cannot be edited");
    }
}
