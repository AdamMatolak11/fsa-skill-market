package sk.posam.fsa.skill_market.domain.service;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;
import sk.posam.fsa.skill_market.domain.profile.UserAlreadyExistsException;
import sk.posam.fsa.skill_market.domain.profile.UserProfile;
import sk.posam.fsa.skill_market.domain.profile.UserProfileCommandRepository;
import sk.posam.fsa.skill_market.domain.profile.UserProfileQueryRepository;
import sk.posam.fsa.skill_market.domain.registration.CreateIdentityUserCommand;
import sk.posam.fsa.skill_market.domain.registration.IdentityRegistrationGateway;
import sk.posam.fsa.skill_market.domain.registration.RegisterUserCommand;

public class RegistrationService implements RegistrationFacade {

    private final UserProfileQueryRepository userProfileQueryRepository;
    private final UserProfileCommandRepository userProfileCommandRepository;
    private final IdentityRegistrationGateway identityRegistrationGateway;

    public RegistrationService(
            UserProfileQueryRepository userProfileQueryRepository,
            UserProfileCommandRepository userProfileCommandRepository,
            IdentityRegistrationGateway identityRegistrationGateway
    ) {
        this.userProfileQueryRepository = userProfileQueryRepository;
        this.userProfileCommandRepository = userProfileCommandRepository;
        this.identityRegistrationGateway = identityRegistrationGateway;
    }

    @Override
    public UserProfile register(RegisterUserCommand command) {
        if (userProfileQueryRepository.findByEmail(command.email()).isPresent()) {
            throw new UserAlreadyExistsException(command.email());
        }

        UUID userId = identityRegistrationGateway.register(new CreateIdentityUserCommand(
                command.email(),
                command.password(),
                command.firstName(),
                command.lastName(),
                command.role()
        ));

        try {
            return userProfileCommandRepository.save(UserProfile.createNew(
                    userId,
                    command.email(),
                    displayName(command.firstName(), command.lastName()),
                    command.role(),
                    Set.of(),
                    OffsetDateTime.now()
            ));
        } catch (RuntimeException exception) {
            identityRegistrationGateway.delete(userId);
            throw exception;
        }
    }

    private String displayName(String firstName, String lastName) {
        return firstName.trim() + " " + lastName.trim();
    }
}
