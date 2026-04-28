package sk.posam.fsa.skill_market.domain.service;

import java.util.List;
import sk.posam.fsa.skill_market.domain.profile.UserProfile;

public interface FreelancerFacade {

    List<UserProfile> searchFreelancers(String skill);
}
