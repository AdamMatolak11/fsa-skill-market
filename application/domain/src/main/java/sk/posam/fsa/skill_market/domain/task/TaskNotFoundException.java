package sk.posam.fsa.skill_market.domain.task;

import java.util.UUID;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(UUID taskId) {
        super("Task '" + taskId + "' was not found");
    }
}
