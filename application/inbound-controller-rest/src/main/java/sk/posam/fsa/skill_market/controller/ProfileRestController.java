package sk.posam.fsa.skill_market.controller;

import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import sk.posam.fsa.skill_market.domain.service.ProfileFacade;
import sk.posam.fsa.skill_market.mapper.ProfileRestMapper;
import sk.posam.fsa.skill_market.rest.api.ProfilesApi;
import sk.posam.fsa.skill_market.rest.dto.UpdateProfileRequest;
import sk.posam.fsa.skill_market.rest.dto.UserProfileResponse;

@RestController
public class ProfileRestController implements ProfilesApi {

    private final ProfileFacade profileFacade;
    private final ProfileRestMapper profileRestMapper;

    public ProfileRestController(ProfileFacade profileFacade, ProfileRestMapper profileRestMapper) {
        this.profileFacade = profileFacade;
        this.profileRestMapper = profileRestMapper;
    }

    @Override
    public ResponseEntity<UserProfileResponse> getProfile(UUID userId) {
        return ResponseEntity.ok(profileRestMapper.toResponse(profileFacade.getProfile(userId)));
    }

    @Override
    public ResponseEntity<UserProfileResponse> updateProfile(UUID userId, UpdateProfileRequest updateProfileRequest) {
        return ResponseEntity.ok(profileRestMapper.toResponse(
                profileFacade.updateProfile(userId, profileRestMapper.toCommand(updateProfileRequest))
        ));
    }
}
