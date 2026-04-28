package sk.posam.fsa.skill_market.jpa;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public class ProjectJpaEntity {

    private UUID id;
    private UUID clientId;
    private UUID assignedFreelancerId;
    private String title;
    private String description;
    private BigDecimal budget;
    private String status;
    private OffsetDateTime createdAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public UUID getAssignedFreelancerId() {
        return assignedFreelancerId;
    }

    public void setAssignedFreelancerId(UUID assignedFreelancerId) {
        this.assignedFreelancerId = assignedFreelancerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
