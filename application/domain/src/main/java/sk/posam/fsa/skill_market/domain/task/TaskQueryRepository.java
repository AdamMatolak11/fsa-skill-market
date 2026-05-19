package sk.posam.fsa.skill_market.domain.task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskQueryRepository {

    List<Task> findByProjectId(UUID projectId);

    Optional<Task> findById(UUID taskId);
}
