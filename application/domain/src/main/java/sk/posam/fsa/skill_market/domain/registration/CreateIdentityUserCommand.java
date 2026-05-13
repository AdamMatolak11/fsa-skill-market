package sk.posam.fsa.skill_market.domain.registration;

import sk.posam.fsa.skill_market.domain.profile.UserRole;

public record CreateIdentityUserCommand(
        String email,
        String password,
        String firstName,
        String lastName,
        UserRole role
) {
}
