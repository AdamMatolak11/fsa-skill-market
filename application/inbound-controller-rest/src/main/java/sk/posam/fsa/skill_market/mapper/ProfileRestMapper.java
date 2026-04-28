package sk.posam.fsa.skill_market.mapper;

import java.util.LinkedHashSet;
import org.springframework.stereotype.Component;
import sk.posam.fsa.skill_market.domain.profile.UpdateUserProfileCommand;
import sk.posam.fsa.skill_market.domain.profile.UserProfile;
import sk.posam.fsa.skill_market.rest.dto.FreelancerResponse;
import sk.posam.fsa.skill_market.rest.dto.UpdateProfileRequest;
import sk.posam.fsa.skill_market.rest.dto.UserProfileResponse;

@Component
public class ProfileRestMapper {

    public UserProfileResponse toResponse(UserProfile profile) {
        UserProfileResponse response = new UserProfileResponse();
        response.setId(profile.id());
        response.setEmail(profile.email());
        response.setDisplayName(profile.displayName());
        response.setBio(profile.bio());
        response.setRole(UserProfileResponse.RoleEnum.fromValue(profile.role().name()));
        response.setSkills(profile.skills().stream().toList());
        response.setAverageRating(profile.averageRating().doubleValue());
        response.setRatingCount(profile.ratingCount());
        response.setCreatedAt(profile.createdAt());
        return response;
    }

    public FreelancerResponse toFreelancerResponse(UserProfile profile) {
        FreelancerResponse response = new FreelancerResponse();
        response.setId(profile.id());
        response.setEmail(profile.email());
        response.setDisplayName(profile.displayName());
        response.setBio(profile.bio());
        response.setRole(FreelancerResponse.RoleEnum.fromValue(profile.role().name()));
        response.setSkills(profile.skills().stream().toList());
        response.setAverageRating(profile.averageRating().doubleValue());
        response.setRatingCount(profile.ratingCount());
        response.setCreatedAt(profile.createdAt());
        return response;
    }

    public UpdateUserProfileCommand toCommand(UpdateProfileRequest request) {
        return new UpdateUserProfileCommand(
                request.getDisplayName(),
                request.getBio(),
                new LinkedHashSet<>(request.getSkills())
        );
    }
}
