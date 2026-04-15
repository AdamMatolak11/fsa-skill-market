package sk.posam.fsa.skill_market.domain.project;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record Project(
    UUID id,
    String title,
    String description,
    BigDecimal budget,
    String status,
    OffsetDateTime createdAt
) {
}
