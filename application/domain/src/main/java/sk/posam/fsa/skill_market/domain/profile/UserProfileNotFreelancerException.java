package sk.posam.fsa.skill_market.domain.profile;

import java.util.UUID;

public class UserProfileNotFreelancerException extends RuntimeException {

    public UserProfileNotFreelancerException(UUID userId) {
        super("User profile with id '" + userId + "' is not a freelancer");
    }
}
