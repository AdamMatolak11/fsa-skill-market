package sk.posam.fsa.skill_market.domain.project;

public interface ProjectCommandRepository {

    boolean existsByTitle(String title);

    Project save(Project project);
}
