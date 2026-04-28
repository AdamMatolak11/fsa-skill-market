package sk.posam.fsa.skill_market.jpa;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public class UserProfileJpaEntity {

    private UUID id;
    private String email;
    private String displayName;
    private String bio;
    private String role;
    private String skillsCsv;
    private BigDecimal averageRating;
    private int ratingCount;
    private OffsetDateTime createdAt;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getSkillsCsv() { return skillsCsv; }
    public void setSkillsCsv(String skillsCsv) { this.skillsCsv = skillsCsv; }
    public BigDecimal getAverageRating() { return averageRating; }
    public void setAverageRating(BigDecimal averageRating) { this.averageRating = averageRating; }
    public int getRatingCount() { return ratingCount; }
    public void setRatingCount(int ratingCount) { this.ratingCount = ratingCount; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}
