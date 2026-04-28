package sk.posam.fsa.skill_market.domain.service;

import java.util.Comparator;
import java.util.List;
import sk.posam.fsa.skill_market.domain.profile.UserProfile;
import sk.posam.fsa.skill_market.domain.profile.UserProfileQueryRepository;

public class FreelancerService implements FreelancerFacade {

    private final UserProfileQueryRepository userProfileQueryRepository;

    public FreelancerService(UserProfileQueryRepository userProfileQueryRepository) {
        this.userProfileQueryRepository = userProfileQueryRepository;
    }

    @Override
    public List<UserProfile> searchFreelancers(String skill) {
        String normalizedSkill = skill == null ? "" : skill.trim();
        return userProfileQueryRepository.findAll().stream()
                .filter(UserProfile::isFreelancer)
                .filter(profile -> normalizedSkill.isEmpty() || profile.hasSkill(normalizedSkill))
                .sorted(Comparator.comparing(UserProfile::averageRating).reversed()
                        .thenComparing(UserProfile::displayName))
                .toList();
    }
}
