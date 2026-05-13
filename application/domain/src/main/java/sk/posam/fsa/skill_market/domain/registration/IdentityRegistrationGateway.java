package sk.posam.fsa.skill_market.domain.registration;

import java.util.UUID;

public interface IdentityRegistrationGateway {

    UUID register(CreateIdentityUserCommand command);

    void delete(UUID userId);
}
