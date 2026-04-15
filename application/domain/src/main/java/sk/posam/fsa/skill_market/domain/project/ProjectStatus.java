package sk.posam.fsa.skill_market.domain.project;

import java.util.Arrays;

public enum ProjectStatus {
    OPEN(true, true),
    IN_PROGRESS(false, false),
    COMPLETED(false, false),
    CANCELLED(false, false);

    private final boolean acceptsOffers;
    private final boolean visibleInMarketplace;

    ProjectStatus(boolean acceptsOffers, boolean visibleInMarketplace) {
        this.acceptsOffers = acceptsOffers;
        this.visibleInMarketplace = visibleInMarketplace;
    }

    public static ProjectStatus from(String value) {
        return Arrays.stream(values())
                .filter(status -> status.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown project status: " + value));
    }

    public boolean acceptsOffers() {
        return acceptsOffers;
    }

    public boolean isVisibleInMarketplace() {
        return visibleInMarketplace;
    }
}
