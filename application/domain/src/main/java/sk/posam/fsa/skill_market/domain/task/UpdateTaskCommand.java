package sk.posam.fsa.skill_market.domain.task;

import java.util.UUID;

public record UpdateTaskCommand(
        UUID projectId,
        UUID taskId,
        UUID assigneeUserId,
        String title,
        String description,
        String status
) {
}
