package sk.posam.fsa.skill_market.domain.profile;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public final class UserProfile {

    private final UUID id;
    private final String email;
    private final String displayName;
    private final String bio;
    private final UserRole role;
    private final Set<String> skills;
    private final BigDecimal averageRating;
    private final int ratingCount;
    private final OffsetDateTime createdAt;

    private UserProfile(
            UUID id,
            String email,
            String displayName,
            String bio,
            UserRole role,
            Set<String> skills,
            BigDecimal averageRating,
            int ratingCount,
            OffsetDateTime createdAt
    ) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.email = normalizeEmail(email);
        this.displayName = normalizeDisplayName(displayName);
        this.bio = normalizeBio(bio);
        this.role = Objects.requireNonNull(role, "role must not be null");
        this.skills = normalizeSkills(skills);
        this.averageRating = normalizeAverageRating(averageRating);
        this.ratingCount = normalizeRatingCount(ratingCount);
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt must not be null");
    }

    public static UserProfile restore(
            UUID id,
            String email,
            String displayName,
            String bio,
            String role,
            Set<String> skills,
            BigDecimal averageRating,
            int ratingCount,
            OffsetDateTime createdAt
    ) {
        return new UserProfile(
                id,
                email,
                displayName,
                bio,
                UserRole.from(role),
                skills,
                averageRating,
                ratingCount,
                createdAt
        );
    }

    public UUID id() {
        return id;
    }

    public String email() {
        return email;
    }

    public String displayName() {
        return displayName;
    }

    public String bio() {
        return bio;
    }

    public UserRole role() {
        return role;
    }

    public Set<String> skills() {
        return skills;
    }

    public BigDecimal averageRating() {
        return averageRating;
    }

    public int ratingCount() {
        return ratingCount;
    }

    public OffsetDateTime createdAt() {
        return createdAt;
    }

    public boolean isFreelancer() {
        return UserRole.FREELANCER.equals(role);
    }

    public boolean hasSkill(String skill) {
        Objects.requireNonNull(skill, "skill must not be null");
        String normalizedSkill = skill.trim().toLowerCase();
        return !normalizedSkill.isEmpty() && skills.stream().anyMatch(existing -> existing.equalsIgnoreCase(normalizedSkill));
    }

    public UserProfile update(String displayName, String bio, Set<String> skills) {
        return new UserProfile(id, email, displayName, bio, role, skills, averageRating, ratingCount, createdAt);
    }

    public UserProfile withNewRating(int score) {
        if (!isFreelancer()) {
            throw new IllegalStateException("Only freelancers can receive ratings");
        }
        if (score < 1 || score > 5) {
            throw new IllegalArgumentException("score must be between 1 and 5");
        }
        BigDecimal totalScore = averageRating.multiply(BigDecimal.valueOf(ratingCount)).add(BigDecimal.valueOf(score));
        int newRatingCount = ratingCount + 1;
        BigDecimal newAverage = totalScore
                .divide(BigDecimal.valueOf(newRatingCount), 2, RoundingMode.HALF_UP)
                .stripTrailingZeros();
        return new UserProfile(id, email, displayName, bio, role, skills, newAverage, newRatingCount, createdAt);
    }

    private static String normalizeEmail(String value) {
        String normalized = requireText(value, "email").toLowerCase();
        if (normalized.length() > 255 || !normalized.contains("@")) {
            throw new IllegalArgumentException("email must be valid and not exceed 255 characters");
        }
        return normalized;
    }

    private static String normalizeDisplayName(String value) {
        String normalized = requireText(value, "displayName");
        if (normalized.length() > 255) {
            throw new IllegalArgumentException("displayName must not exceed 255 characters");
        }
        return normalized;
    }

    private static String normalizeBio(String value) {
        String normalized = value == null ? "" : value.trim();
        if (normalized.length() > 5000) {
            throw new IllegalArgumentException("bio must not exceed 5000 characters");
        }
        return normalized;
    }

    private static Set<String> normalizeSkills(Set<String> values) {
        Objects.requireNonNull(values, "skills must not be null");
        LinkedHashSet<String> normalized = values.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(skill -> !skill.isEmpty())
                .map(String::toLowerCase)
                .collect(LinkedHashSet::new, Set::add, Set::addAll);
        return Set.copyOf(normalized);
    }

    private static BigDecimal normalizeAverageRating(BigDecimal value) {
        Objects.requireNonNull(value, "averageRating must not be null");
        if (value.signum() < 0 || value.compareTo(BigDecimal.valueOf(5)) > 0) {
            throw new IllegalArgumentException("averageRating must be between 0 and 5");
        }
        return value.stripTrailingZeros();
    }

    private static int normalizeRatingCount(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("ratingCount must not be negative");
        }
        return value;
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
