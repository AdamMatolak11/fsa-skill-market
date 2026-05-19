package sk.posam.fsa.skill_market.jpa;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface TaskSpringDataRepository extends JpaRepository<TaskJpaEntity, UUID> {

    List<TaskJpaEntity> findAllByProjectIdOrderByCreatedAtAsc(UUID projectId);
}
