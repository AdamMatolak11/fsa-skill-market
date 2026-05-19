package sk.posam.fsa.skill_market.jpa;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface TaskCommentSpringDataRepository extends JpaRepository<TaskCommentJpaEntity, UUID> {

    List<TaskCommentJpaEntity> findAllByTaskIdOrderByCreatedAtAsc(UUID taskId);
}
