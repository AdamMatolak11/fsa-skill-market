package sk.posam.fsa.skill_market.domain.rating;

import java.util.UUID;

public record CreateRatingCommand(
        UUID projectId,
        UUID clientId,
        UUID freelancerId,
        int score,
        String comment
) {
}
