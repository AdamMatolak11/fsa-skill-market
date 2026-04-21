package sk.posam.fsa.skill_market.domain.project;

import java.util.Comparator;
import java.util.List;

public final class ProjectCatalog {

    private final List<Project> projects;

    private ProjectCatalog(List<Project> projects) {
        this.projects = List.copyOf(projects);
    }

    public static ProjectCatalog of(List<Project> projects) {
        return new ProjectCatalog(projects);
    }

    public List<Project> projectsVisibleInMarketplace() {
        return projects.stream()
                .filter(Project::isVisibleInMarketplace)
                .sorted(Comparator.comparing(Project::createdAt).reversed())
                .toList();
    }
}
