package sk.posam.fsa.skill_market.mapper;

import org.springframework.stereotype.Component;
import sk.posam.fsa.skill_market.domain.profile.UserRole;
import sk.posam.fsa.skill_market.domain.registration.RegisterUserCommand;
import sk.posam.fsa.skill_market.rest.dto.RegisterUserRequest;

@Component
public class RegistrationRestMapper {

    public RegisterUserCommand toCommand(RegisterUserRequest request) {
        return new RegisterUserCommand(
                request.getEmail(),
                request.getPassword(),
                request.getFirstName(),
                request.getLastName(),
                UserRole.from(request.getRole().getValue())
        );
    }
}
