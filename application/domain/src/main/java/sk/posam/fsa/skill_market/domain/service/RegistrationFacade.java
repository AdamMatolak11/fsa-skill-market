package sk.posam.fsa.skill_market.domain.service;

import sk.posam.fsa.skill_market.domain.profile.UserProfile;
import sk.posam.fsa.skill_market.domain.registration.RegisterUserCommand;

public interface RegistrationFacade {

    UserProfile register(RegisterUserCommand command);
}
