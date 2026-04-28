package sk.posam.fsa.skill_market.jpa;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface UserProfileSpringDataRepository extends JpaRepository<UserProfileJpaEntity, UUID> {
}
