package sk.posam.fsa.skill_market.jpa;

import java.time.OffsetDateTime;
import java.util.UUID;

public class RatingJpaEntity {

    private UUID id;
    private UUID projectId;
    private UUID clientId;
    private UUID freelancerId;
    private int score;
    private String comment;
    private OffsetDateTime createdAt;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getProjectId() { return projectId; }
    public void setProjectId(UUID projectId) { this.projectId = projectId; }
    public UUID getClientId() { return clientId; }
    public void setClientId(UUID clientId) { this.clientId = clientId; }
    public UUID getFreelancerId() { return freelancerId; }
    public void setFreelancerId(UUID freelancerId) { this.freelancerId = freelancerId; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}
