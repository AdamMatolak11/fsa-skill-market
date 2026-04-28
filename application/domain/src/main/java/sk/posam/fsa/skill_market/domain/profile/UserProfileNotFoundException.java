package sk.posam.fsa.skill_market.domain.profile;

import java.util.UUID;

public class UserProfileNotFoundException extends RuntimeException {

    public UserProfileNotFoundException(UUID userId) {
        super("User profile with id '" + userId + "' was not found");
    }
}
