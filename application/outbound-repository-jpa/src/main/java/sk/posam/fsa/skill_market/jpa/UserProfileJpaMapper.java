package sk.posam.fsa.skill_market.jpa;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import org.springframework.stereotype.Component;
import sk.posam.fsa.skill_market.domain.profile.UserProfile;

@Component
public class UserProfileJpaMapper {

    public UserProfile toDomain(UserProfileJpaEntity entity) {
        return UserProfile.restore(
                entity.getId(),
                entity.getEmail(),
                entity.getDisplayName(),
                entity.getBio(),
                entity.getRole(),
                toSkills(entity.getSkillsCsv()),
                entity.getAverageRating(),
                entity.getRatingCount(),
                entity.getCreatedAt()
        );
    }

    public UserProfileJpaEntity toEntity(UserProfile profile) {
        UserProfileJpaEntity entity = new UserProfileJpaEntity();
        entity.setId(profile.id());
        entity.setEmail(profile.email());
        entity.setDisplayName(profile.displayName());
        entity.setBio(profile.bio());
        entity.setRole(profile.role().name());
        entity.setSkillsCsv(String.join(",", profile.skills()));
        entity.setAverageRating(profile.averageRating());
        entity.setRatingCount(profile.ratingCount());
        entity.setCreatedAt(profile.createdAt());
        return entity;
    }

    private Set<String> toSkills(String value) {
        if (value == null || value.isBlank()) {
            return Set.of();
        }
        return Arrays.stream(value.split(","))
                .map(String::trim)
                .filter(skill -> !skill.isEmpty())
                .collect(LinkedHashSet::new, Set::add, Set::addAll);
    }
}
