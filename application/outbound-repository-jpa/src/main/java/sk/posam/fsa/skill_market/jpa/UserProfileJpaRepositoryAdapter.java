package sk.posam.fsa.skill_market.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;
import sk.posam.fsa.skill_market.domain.profile.UserProfile;
import sk.posam.fsa.skill_market.domain.profile.UserProfileCommandRepository;
import sk.posam.fsa.skill_market.domain.profile.UserProfileQueryRepository;

@Repository
public class UserProfileJpaRepositoryAdapter implements UserProfileQueryRepository, UserProfileCommandRepository {

    private final UserProfileSpringDataRepository userProfileSpringDataRepository;
    private final UserProfileJpaMapper userProfileJpaMapper;

    public UserProfileJpaRepositoryAdapter(
            UserProfileSpringDataRepository userProfileSpringDataRepository,
            UserProfileJpaMapper userProfileJpaMapper
    ) {
        this.userProfileSpringDataRepository = userProfileSpringDataRepository;
        this.userProfileJpaMapper = userProfileJpaMapper;
    }

    @Override
    public List<UserProfile> findAll() {
        return userProfileSpringDataRepository.findAll().stream()
                .map(userProfileJpaMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<UserProfile> findById(UUID userId) {
        return userProfileSpringDataRepository.findById(userId)
                .map(userProfileJpaMapper::toDomain);
    }

    @Override
    public Optional<UserProfile> findByEmail(String email) {
        return userProfileSpringDataRepository.findByEmailIgnoreCase(email)
                .map(userProfileJpaMapper::toDomain);
    }

    @Override
    public UserProfile save(UserProfile profile) {
        return userProfileJpaMapper.toDomain(userProfileSpringDataRepository.save(userProfileJpaMapper.toEntity(profile)));
    }
}
