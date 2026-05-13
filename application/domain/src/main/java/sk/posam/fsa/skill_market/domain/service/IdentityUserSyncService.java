package sk.posam.fsa.skill_market.domain.service;

import java.time.OffsetDateTime;
import java.util.Set;
import sk.posam.fsa.skill_market.domain.profile.SyncIdentityUserCommand;
import sk.posam.fsa.skill_market.domain.profile.UserAlreadyExistsException;
import sk.posam.fsa.skill_market.domain.profile.UserProfile;
import sk.posam.fsa.skill_market.domain.profile.UserProfileCommandRepository;
import sk.posam.fsa.skill_market.domain.profile.UserProfileQueryRepository;

public class IdentityUserSyncService implements IdentityUserSyncFacade {

    private final UserProfileQueryRepository userProfileQueryRepository;
    private final UserProfileCommandRepository userProfileCommandRepository;

    public IdentityUserSyncService(
            UserProfileQueryRepository userProfileQueryRepository,
            UserProfileCommandRepository userProfileCommandRepository
    ) {
        this.userProfileQueryRepository = userProfileQueryRepository;
        this.userProfileCommandRepository = userProfileCommandRepository;
    }

    @Override
    public UserProfile syncRegisteredUser(SyncIdentityUserCommand command) {
        return userProfileQueryRepository.findById(command.userId())
                .orElseGet(() -> createNewProfile(command));
    }

    private UserProfile createNewProfile(SyncIdentityUserCommand command) {
        if (userProfileQueryRepository.findByEmail(command.email()).isPresent()) {
            throw new UserAlreadyExistsException(command.email());
        }

        return userProfileCommandRepository.save(UserProfile.createNew(
                command.userId(),
                command.email(),
                displayName(command.firstName(), command.lastName(), command.email()),
                command.role(),
                Set.of(),
                OffsetDateTime.now()
        ));
    }

    private String displayName(String firstName, String lastName, String email) {
        String fullName = ((firstName == null ? "" : firstName.trim()) + " " + (lastName == null ? "" : lastName.trim())).trim();
        if (!fullName.isEmpty()) {
            return fullName;
        }
        return email.trim();
    }
}
