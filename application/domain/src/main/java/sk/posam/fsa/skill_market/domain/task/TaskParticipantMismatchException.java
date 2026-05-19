package sk.posam.fsa.skill_market.domain.task;

import java.util.UUID;

public class TaskParticipantMismatchException extends RuntimeException {

    public TaskParticipantMismatchException(UUID projectId, UUID userId) {
        super("User '" + userId + "' is not a participant of project '" + projectId + "'");
    }
}
