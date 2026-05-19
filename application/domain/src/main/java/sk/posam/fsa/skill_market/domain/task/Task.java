package sk.posam.fsa.skill_market.domain.task;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

public final class Task {

    private final UUID id;
    private final UUID projectId;
    private final UUID assigneeUserId;
    private final UUID createdByUserId;
    private final String title;
    private final String description;
    private final TaskStatus status;
    private final OffsetDateTime createdAt;
    private final OffsetDateTime updatedAt;

    private Task(
            UUID id,
            UUID projectId,
            UUID assigneeUserId,
            UUID createdByUserId,
            String title,
            String description,
            TaskStatus status,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt
    ) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.projectId = Objects.requireNonNull(projectId, "projectId must not be null");
        this.assigneeUserId = assigneeUserId;
        this.createdByUserId = Objects.requireNonNull(createdByUserId, "createdByUserId must not be null");
        this.title = normalizeTitle(title);
        this.description = normalizeDescription(description);
        this.status = Objects.requireNonNull(status, "status must not be null");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt must not be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt must not be null");
    }

    public static Task restore(
            UUID id,
            UUID projectId,
            UUID assigneeUserId,
            UUID createdByUserId,
            String title,
            String description,
            String status,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt
    ) {
        return new Task(
                id,
                projectId,
                assigneeUserId,
                createdByUserId,
                title,
                description,
                TaskStatus.from(status),
                createdAt,
                updatedAt
        );
    }

    public static Task createNew(
            UUID projectId,
            UUID assigneeUserId,
            UUID createdByUserId,
            String title,
            String description,
            OffsetDateTime now
    ) {
        return new Task(
                UUID.randomUUID(),
                projectId,
                assigneeUserId,
                createdByUserId,
                title,
                description,
                TaskStatus.TODO,
                now,
                now
        );
    }

    public UUID id() {
        return id;
    }

    public UUID projectId() {
        return projectId;
    }

    public UUID assigneeUserId() {
        return assigneeUserId;
    }

    public UUID createdByUserId() {
        return createdByUserId;
    }

    public String title() {
        return title;
    }

    public String description() {
        return description;
    }

    public TaskStatus status() {
        return status;
    }

    public OffsetDateTime createdAt() {
        return createdAt;
    }

    public OffsetDateTime updatedAt() {
        return updatedAt;
    }

    public Task update(String title, String description, TaskStatus status, UUID assigneeUserId, OffsetDateTime updatedAt) {
        return new Task(
                id,
                projectId,
                assigneeUserId,
                createdByUserId,
                title,
                description,
                status,
                createdAt,
                Objects.requireNonNull(updatedAt, "updatedAt must not be null")
        );
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

    private static String requireText(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName + " must not be null");
        String normalized = value.trim();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        return normalized;
    }
}
