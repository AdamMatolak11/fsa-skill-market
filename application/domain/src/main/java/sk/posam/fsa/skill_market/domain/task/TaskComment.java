package sk.posam.fsa.skill_market.domain.task;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

public final class TaskComment {

    private final UUID id;
    private final UUID taskId;
    private final UUID authorUserId;
    private final String message;
    private final OffsetDateTime createdAt;

    private TaskComment(
            UUID id,
            UUID taskId,
            UUID authorUserId,
            String message,
            OffsetDateTime createdAt
    ) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.taskId = Objects.requireNonNull(taskId, "taskId must not be null");
        this.authorUserId = Objects.requireNonNull(authorUserId, "authorUserId must not be null");
        this.message = normalizeMessage(message);
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt must not be null");
    }

    public static TaskComment restore(
            UUID id,
            UUID taskId,
            UUID authorUserId,
            String message,
            OffsetDateTime createdAt
    ) {
        return new TaskComment(id, taskId, authorUserId, message, createdAt);
    }

    public static TaskComment createNew(UUID taskId, UUID authorUserId, String message, OffsetDateTime createdAt) {
        return new TaskComment(UUID.randomUUID(), taskId, authorUserId, message, createdAt);
    }

    public UUID id() {
        return id;
    }

    public UUID taskId() {
        return taskId;
    }

    public UUID authorUserId() {
        return authorUserId;
    }

    public String message() {
        return message;
    }

    public OffsetDateTime createdAt() {
        return createdAt;
    }

    private static String normalizeMessage(String message) {
        Objects.requireNonNull(message, "message must not be null");
        String normalized = message.trim();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("message must not be blank");
        }
        if (normalized.length() > 5000) {
            throw new IllegalArgumentException("message must not exceed 5000 characters");
        }
        return normalized;
    }
}
