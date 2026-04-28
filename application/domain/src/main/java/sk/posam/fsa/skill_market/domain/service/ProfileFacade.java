package sk.posam.fsa.skill_market.domain.service;

import java.util.UUID;
import sk.posam.fsa.skill_market.domain.profile.UpdateUserProfileCommand;
import sk.posam.fsa.skill_market.domain.profile.UserProfile;

public interface ProfileFacade {

    UserProfile getProfile(UUID userId);

    UserProfile updateProfile(UUID userId, UpdateUserProfileCommand command);
}
