package sk.posam.fsa.skill_market.domain.task;

import java.util.List;
import java.util.UUID;

public interface TaskCommentQueryRepository {

    List<TaskComment> findByTaskId(UUID taskId);
}
