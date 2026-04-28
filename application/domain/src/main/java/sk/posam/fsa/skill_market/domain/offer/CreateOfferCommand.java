package sk.posam.fsa.skill_market.domain.offer;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateOfferCommand(
        UUID projectId,
        UUID freelancerId,
        BigDecimal amount,
        String message
) {
}
