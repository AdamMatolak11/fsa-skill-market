package sk.posam.fsa.skill_market.domain.offer;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

public final class Offer {

    private final UUID id;
    private final UUID projectId;
    private final UUID freelancerId;
    private final BigDecimal amount;
    private final String message;
    private final OfferStatus status;
    private final OffsetDateTime createdAt;

    private Offer(
            UUID id,
            UUID projectId,
            UUID freelancerId,
            BigDecimal amount,
            String message,
            OfferStatus status,
            OffsetDateTime createdAt
    ) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.projectId = Objects.requireNonNull(projectId, "projectId must not be null");
        this.freelancerId = Objects.requireNonNull(freelancerId, "freelancerId must not be null");
        this.amount = normalizeAmount(amount);
        this.message = normalizeMessage(message);
        this.status = Objects.requireNonNull(status, "status must not be null");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt must not be null");
    }

    public static Offer createNew(UUID projectId, UUID freelancerId, BigDecimal amount, String message, OffsetDateTime createdAt) {
        return new Offer(UUID.randomUUID(), projectId, freelancerId, amount, message, OfferStatus.PENDING, createdAt);
    }

    public static Offer restore(
            UUID id,
            UUID projectId,
            UUID freelancerId,
            BigDecimal amount,
            String message,
            String status,
            OffsetDateTime createdAt
    ) {
        return new Offer(id, projectId, freelancerId, amount, message, OfferStatus.from(status), createdAt);
    }

    public UUID id() { return id; }
    public UUID projectId() { return projectId; }
    public UUID freelancerId() { return freelancerId; }
    public BigDecimal amount() { return amount; }
    public String message() { return message; }
    public OfferStatus status() { return status; }
    public OffsetDateTime createdAt() { return createdAt; }
    public boolean canBeCancelled() { return status == OfferStatus.PENDING; }
    public boolean canBeDecided() { return status == OfferStatus.PENDING; }

    public Offer accept() {
        if (!canBeDecided()) {
            throw new IllegalStateException("Offer cannot be accepted in status '" + status + "'");
        }
        return new Offer(id, projectId, freelancerId, amount, message, OfferStatus.ACCEPTED, createdAt);
    }

    public Offer reject() {
        if (!canBeDecided()) {
            throw new IllegalStateException("Offer cannot be rejected in status '" + status + "'");
        }
        return new Offer(id, projectId, freelancerId, amount, message, OfferStatus.REJECTED, createdAt);
    }

    public Offer cancel() {
        if (!canBeCancelled()) {
            throw new IllegalStateException("Offer cannot be cancelled in status '" + status + "'");
        }
        return new Offer(id, projectId, freelancerId, amount, message, OfferStatus.CANCELLED, createdAt);
    }

    private static BigDecimal normalizeAmount(BigDecimal value) {
        Objects.requireNonNull(value, "amount must not be null");
        if (value.signum() <= 0) {
            throw new IllegalArgumentException("amount must be positive");
        }
        return value.stripTrailingZeros();
    }

    private static String normalizeMessage(String value) {
        Objects.requireNonNull(value, "message must not be null");
        String normalized = value.trim();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("message must not be blank");
        }
        if (normalized.length() > 5000) {
            throw new IllegalArgumentException("message must not exceed 5000 characters");
        }
        return normalized;
    }
}
