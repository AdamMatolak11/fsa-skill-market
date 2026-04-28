package sk.posam.fsa.skill_market.domain.project;

import java.util.UUID;

public class ProjectNotCompletedException extends RuntimeException {

    public ProjectNotCompletedException(UUID projectId, ProjectStatus status) {
        super("Project with id '" + projectId + "' is in status '" + status + "' and cannot be rated yet");
    }
}
