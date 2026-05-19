package sk.posam.fsa.skill_market.domain.task;

import java.util.UUID;

public record CreateTaskCommand(
        UUID projectId,
        UUID creatorUserId,
        UUID assigneeUserId,
        String title,
        String description
) {
}
