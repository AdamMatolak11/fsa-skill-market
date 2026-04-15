package sk.posam.fsa.skill_market.domain.project;

import java.math.BigDecimal;

public record CreateProjectCommand(
        String title,
        String description,
        BigDecimal budget
) {
}
