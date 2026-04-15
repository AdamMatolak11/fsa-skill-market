package sk.posam.fsa.skill_market.domain.project;

import java.util.List;

public interface ProjectQueryRepository {

    List<Project> findAll();
}
