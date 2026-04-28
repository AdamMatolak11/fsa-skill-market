package sk.posam.fsa.skill_market.domain.profile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserProfileQueryRepository {

    List<UserProfile> findAll();

    Optional<UserProfile> findById(UUID userId);

    Optional<UserProfile> findByEmail(String email);
}
