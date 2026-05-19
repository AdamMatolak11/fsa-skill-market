package sk.posam.fsa.skill_market.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;
import sk.posam.fsa.skill_market.domain.task.Task;
import sk.posam.fsa.skill_market.domain.task.TaskCommandRepository;
import sk.posam.fsa.skill_market.domain.task.TaskQueryRepository;

@Repository
public class TaskJpaRepositoryAdapter implements TaskQueryRepository, TaskCommandRepository {

    private final TaskSpringDataRepository taskSpringDataRepository;
    private final TaskJpaMapper taskJpaMapper;

    public TaskJpaRepositoryAdapter(
            TaskSpringDataRepository taskSpringDataRepository,
            TaskJpaMapper taskJpaMapper
    ) {
        this.taskSpringDataRepository = taskSpringDataRepository;
        this.taskJpaMapper = taskJpaMapper;
    }

    @Override
    public List<Task> findByProjectId(UUID projectId) {
        return taskSpringDataRepository.findAllByProjectIdOrderByCreatedAtAsc(projectId).stream()
                .map(taskJpaMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Task> findById(UUID taskId) {
        return taskSpringDataRepository.findById(taskId)
                .map(taskJpaMapper::toDomain);
    }

    @Override
    public Task save(Task task) {
        return taskJpaMapper.toDomain(taskSpringDataRepository.save(taskJpaMapper.toEntity(task)));
    }
}
