package sk.posam.fsa.skill_market.jpa;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;
import sk.posam.fsa.skill_market.domain.task.TaskComment;
import sk.posam.fsa.skill_market.domain.task.TaskCommentCommandRepository;
import sk.posam.fsa.skill_market.domain.task.TaskCommentQueryRepository;

@Repository
public class TaskCommentJpaRepositoryAdapter implements TaskCommentQueryRepository, TaskCommentCommandRepository {

    private final TaskCommentSpringDataRepository taskCommentSpringDataRepository;
    private final TaskCommentJpaMapper taskCommentJpaMapper;

    public TaskCommentJpaRepositoryAdapter(
            TaskCommentSpringDataRepository taskCommentSpringDataRepository,
            TaskCommentJpaMapper taskCommentJpaMapper
    ) {
        this.taskCommentSpringDataRepository = taskCommentSpringDataRepository;
        this.taskCommentJpaMapper = taskCommentJpaMapper;
    }

    @Override
    public List<TaskComment> findByTaskId(UUID taskId) {
        return taskCommentSpringDataRepository.findAllByTaskIdOrderByCreatedAtAsc(taskId).stream()
                .map(taskCommentJpaMapper::toDomain)
                .toList();
    }

    @Override
    public TaskComment save(TaskComment taskComment) {
        return taskCommentJpaMapper.toDomain(taskCommentSpringDataRepository.save(taskCommentJpaMapper.toEntity(taskComment)));
    }
}
