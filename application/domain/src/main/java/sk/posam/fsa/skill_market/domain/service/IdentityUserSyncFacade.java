package sk.posam.fsa.skill_market.domain.service;

import sk.posam.fsa.skill_market.domain.profile.SyncIdentityUserCommand;
import sk.posam.fsa.skill_market.domain.profile.UserProfile;

public interface IdentityUserSyncFacade {

    UserProfile syncRegisteredUser(SyncIdentityUserCommand command);
}
