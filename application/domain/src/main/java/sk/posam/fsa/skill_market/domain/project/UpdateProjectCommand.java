package sk.posam.fsa.skill_market.domain.project;

import java.math.BigDecimal;
import java.util.UUID;

public record UpdateProjectCommand(
        UUID projectId,
        String title,
        String description,
        BigDecimal budget
) {
}
