package sk.posam.fsa.skill_market.registration;

import java.util.UUID;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import sk.posam.fsa.skill_market.domain.registration.CreateIdentityUserCommand;
import sk.posam.fsa.skill_market.domain.registration.IdentityRegistrationGateway;

@Component
@Profile("!keycloak")
public class LocalIdentityRegistrationGateway implements IdentityRegistrationGateway {

    @Override
    public UUID register(CreateIdentityUserCommand command) {
        return UUID.randomUUID();
    }

    @Override
    public void delete(UUID userId) {
    }
}
