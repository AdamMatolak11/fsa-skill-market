package sk.posam.fsa.skill_market.domain.profile;

import java.util.UUID;

public record SyncIdentityUserCommand(
        UUID userId,
        String email,
        String firstName,
        String lastName,
        UserRole role
) {
}
