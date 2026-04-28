package sk.posam.fsa.skill_market.jpa;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public class OfferJpaEntity {

    private UUID id;
    private UUID projectId;
    private UUID freelancerId;
    private BigDecimal amount;
    private String message;
    private String status;
    private OffsetDateTime createdAt;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getProjectId() { return projectId; }
    public void setProjectId(UUID projectId) { this.projectId = projectId; }
    public UUID getFreelancerId() { return freelancerId; }
    public void setFreelancerId(UUID freelancerId) { this.freelancerId = freelancerId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}
