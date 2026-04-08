package sk.posam.fsa.skill_market.infrastructure.persistence.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import sk.posam.fsa.skill_market.infrastructure.persistence.entity.ProjectJpaEntity;

public interface ProjectJpaRepository extends JpaRepository<ProjectJpaEntity, UUID> {
}
