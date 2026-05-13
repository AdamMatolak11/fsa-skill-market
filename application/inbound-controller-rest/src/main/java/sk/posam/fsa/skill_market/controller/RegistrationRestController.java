package sk.posam.fsa.skill_market.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import sk.posam.fsa.skill_market.domain.service.RegistrationFacade;
import sk.posam.fsa.skill_market.mapper.ProfileRestMapper;
import sk.posam.fsa.skill_market.mapper.RegistrationRestMapper;
import sk.posam.fsa.skill_market.rest.api.RegistrationsApi;
import sk.posam.fsa.skill_market.rest.dto.RegisterUserRequest;
import sk.posam.fsa.skill_market.rest.dto.UserProfileResponse;

@RestController
public class RegistrationRestController implements RegistrationsApi {

    private final RegistrationFacade registrationFacade;
    private final RegistrationRestMapper registrationRestMapper;
    private final ProfileRestMapper profileRestMapper;

    public RegistrationRestController(
            RegistrationFacade registrationFacade,
            RegistrationRestMapper registrationRestMapper,
            ProfileRestMapper profileRestMapper
    ) {
        this.registrationFacade = registrationFacade;
        this.registrationRestMapper = registrationRestMapper;
        this.profileRestMapper = profileRestMapper;
    }

    @Override
    public ResponseEntity<UserProfileResponse> registerUser(RegisterUserRequest registerUserRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(profileRestMapper.toResponse(
                        registrationFacade.register(registrationRestMapper.toCommand(registerUserRequest))
                ));
    }
}
