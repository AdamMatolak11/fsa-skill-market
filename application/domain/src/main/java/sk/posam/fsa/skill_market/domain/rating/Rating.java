package sk.posam.fsa.skill_market.domain.rating;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

public final class Rating {

    private final UUID id;
    private final UUID projectId;
    private final UUID clientId;
    private final UUID freelancerId;
    private final int score;
    private final String comment;
    private final OffsetDateTime createdAt;

    private Rating(
            UUID id,
            UUID projectId,
            UUID clientId,
            UUID freelancerId,
            int score,
            String comment,
            OffsetDateTime createdAt
    ) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.projectId = Objects.requireNonNull(projectId, "projectId must not be null");
        this.clientId = Objects.requireNonNull(clientId, "clientId must not be null");
        this.freelancerId = Objects.requireNonNull(freelancerId, "freelancerId must not be null");
        this.score = normalizeScore(score);
        this.comment = normalizeComment(comment);
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt must not be null");
    }

    public static Rating createNew(
            UUID projectId,
            UUID clientId,
            UUID freelancerId,
            int score,
            String comment,
            OffsetDateTime createdAt
    ) {
        return new Rating(UUID.randomUUID(), projectId, clientId, freelancerId, score, comment, createdAt);
    }

    public static Rating restore(
            UUID id,
            UUID projectId,
            UUID clientId,
            UUID freelancerId,
            int score,
            String comment,
            OffsetDateTime createdAt
    ) {
        return new Rating(id, projectId, clientId, freelancerId, score, comment, createdAt);
    }

    public UUID id() { return id; }
    public UUID projectId() { return projectId; }
    public UUID clientId() { return clientId; }
    public UUID freelancerId() { return freelancerId; }
    public int score() { return score; }
    public String comment() { return comment; }
    public OffsetDateTime createdAt() { return createdAt; }

    private static int normalizeScore(int value) {
        if (value < 1 || value > 5) {
            throw new IllegalArgumentException("score must be between 1 and 5");
        }
        return value;
    }

    private static String normalizeComment(String value) {
        Objects.requireNonNull(value, "comment must not be null");
        String normalized = value.trim();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("comment must not be blank");
        }
        if (normalized.length() > 5000) {
            throw new IllegalArgumentException("comment must not exceed 5000 characters");
        }
        return normalized;
    }
}
