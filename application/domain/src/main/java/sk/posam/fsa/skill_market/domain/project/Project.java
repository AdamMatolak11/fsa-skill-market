package sk.posam.fsa.skill_market.domain.project;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

public final class Project {

    private final UUID id;
    private final String title;
    private final String description;
    private final BigDecimal budget;
    private final ProjectStatus status;
    private final OffsetDateTime createdAt;

    private Project(
            UUID id,
            String title,
            String description,
            BigDecimal budget,
            ProjectStatus status,
            OffsetDateTime createdAt
    ) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.title = normalizeTitle(title);
        this.description = normalizeDescription(description);
        this.budget = normalizeBudget(budget);
        this.status = Objects.requireNonNull(status, "status must not be null");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt must not be null");
    }

    public static Project restore(
            UUID id,
            String title,
            String description,
            BigDecimal budget,
            String status,
            OffsetDateTime createdAt
    ) {
        return new Project(id, title, description, budget, ProjectStatus.from(status), createdAt);
    }

    public UUID id() {
        return id;
    }

    public String title() {
        return title;
    }

    public String description() {
        return description;
    }

    public BigDecimal budget() {
        return budget;
    }

    public ProjectStatus status() {
        return status;
    }

    public OffsetDateTime createdAt() {
        return createdAt;
    }

    public boolean isVisibleInMarketplace() {
        return status.isVisibleInMarketplace();
    }

    public boolean acceptsOffers() {
        return status.acceptsOffers();
    }

    private static String normalizeTitle(String title) {
        String normalized = requireText(title, "title");
        if (normalized.length() > 255) {
            throw new IllegalArgumentException("title must not exceed 255 characters");
        }
        return normalized;
    }

    private static String normalizeDescription(String description) {
        String normalized = requireText(description, "description");
        if (normalized.length() > 5000) {
            throw new IllegalArgumentException("description must not exceed 5000 characters");
        }
        return normalized;
    }

    private static BigDecimal normalizeBudget(BigDecimal budget) {
        Objects.requireNonNull(budget, "budget must not be null");
        if (budget.signum() <= 0) {
            throw new IllegalArgumentException("budget must be positive");
        }
        return budget.stripTrailingZeros();
    }

    private static String requireText(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName + " must not be null");
        String normalized = value.trim();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        return normalized;
    }
}
