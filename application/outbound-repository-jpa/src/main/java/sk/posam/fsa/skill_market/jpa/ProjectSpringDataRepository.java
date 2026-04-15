package sk.posam.fsa.skill_market.jpa;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface ProjectSpringDataRepository extends JpaRepository<ProjectJpaEntity, UUID> {

    boolean existsByTitleIgnoreCase(String title);
}
