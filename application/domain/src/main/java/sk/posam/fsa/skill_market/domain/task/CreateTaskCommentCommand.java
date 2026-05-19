package sk.posam.fsa.skill_market.domain.task;

import java.util.UUID;

public record CreateTaskCommentCommand(
        UUID projectId,
        UUID taskId,
        UUID authorUserId,
        String message
) {
}
