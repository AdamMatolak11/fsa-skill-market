package sk.posam.fsa.skill_market.domain.profile;

import java.util.Set;

public record UpdateUserProfileCommand(
        String displayName,
        String bio,
        Set<String> skills
) {
}
