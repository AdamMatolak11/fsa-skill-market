package sk.posam.fsa.skill_market.jpa;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface OfferSpringDataRepository extends JpaRepository<OfferJpaEntity, UUID> {

    java.util.List<OfferJpaEntity> findByProjectIdOrderByCreatedAtDesc(UUID projectId);
}
