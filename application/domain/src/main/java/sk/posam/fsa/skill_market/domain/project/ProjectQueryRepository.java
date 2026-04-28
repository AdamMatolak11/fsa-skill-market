package sk.posam.fsa.skill_market.domain.project;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectQueryRepository {

    List<Project> findAll();

    Optional<Project> findById(UUID id);
}
