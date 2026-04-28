package sk.posam.fsa.skill_market.domain.service;

import java.util.UUID;
import sk.posam.fsa.skill_market.domain.profile.UpdateUserProfileCommand;
import sk.posam.fsa.skill_market.domain.profile.UserProfile;
import sk.posam.fsa.skill_market.domain.profile.UserProfileCommandRepository;
import sk.posam.fsa.skill_market.domain.profile.UserProfileNotFoundException;
import sk.posam.fsa.skill_market.domain.profile.UserProfileQueryRepository;

public class ProfileService implements ProfileFacade {

    private final UserProfileQueryRepository userProfileQueryRepository;
    private final UserProfileCommandRepository userProfileCommandRepository;

    public ProfileService(
            UserProfileQueryRepository userProfileQueryRepository,
            UserProfileCommandRepository userProfileCommandRepository
    ) {
        this.userProfileQueryRepository = userProfileQueryRepository;
        this.userProfileCommandRepository = userProfileCommandRepository;
    }

    @Override
    public UserProfile getProfile(UUID userId) {
        return userProfileQueryRepository.findById(userId)
                .orElseThrow(() -> new UserProfileNotFoundException(userId));
    }

    @Override
    public UserProfile updateProfile(UUID userId, UpdateUserProfileCommand command) {
        UserProfile existingProfile = getProfile(userId);
        UserProfile updatedProfile = existingProfile.update(command.displayName(), command.bio(), command.skills());
        return userProfileCommandRepository.save(updatedProfile);
    }
}
