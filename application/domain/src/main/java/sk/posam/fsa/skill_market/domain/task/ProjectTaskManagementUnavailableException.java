package sk.posam.fsa.skill_market.domain.task;

import java.util.UUID;

public class ProjectTaskManagementUnavailableException extends RuntimeException {

    public ProjectTaskManagementUnavailableException(UUID projectId) {
        super("Project '" + projectId + "' is not ready for task management because no freelancer has been assigned yet");
    }
}
