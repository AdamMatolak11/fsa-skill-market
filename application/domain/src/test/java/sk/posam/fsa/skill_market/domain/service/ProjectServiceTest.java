package sk.posam.fsa.skill_market.domain.service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import sk.posam.fsa.skill_market.domain.project.Project;
import sk.posam.fsa.skill_market.domain.project.ProjectQueryRepository;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ProjectServiceTest {

    @Test
    void getAllProjects_returns_only_marketplace_visible_projects_sorted_newest_first() {
        Project olderOpenProject = Project.restore(
                UUID.randomUUID(),
                "Legacy modernization",
                "Refactor a monolith",
                BigDecimal.valueOf(3000),
                "OPEN",
                OffsetDateTime.parse("2026-01-10T10:15:30+01:00")
        );
        Project hiddenProject = Project.restore(
                UUID.randomUUID(),
                "Private delivery",
                "Already assigned work",
                BigDecimal.valueOf(2000),
                "IN_PROGRESS",
                OffsetDateTime.parse("2026-02-10T10:15:30+01:00")
        );
        Project newerOpenProject = Project.restore(
                UUID.randomUUID(),
                "Greenfield portal",
                "Build the MVP",
                BigDecimal.valueOf(8000),
                "OPEN",
                OffsetDateTime.parse("2026-03-10T10:15:30+01:00")
        );

        ProjectQueryRepository repository = () -> List.of(olderOpenProject, hiddenProject, newerOpenProject);

        ProjectService service = new ProjectService(repository);

        assertEquals(List.of(newerOpenProject, olderOpenProject), service.getAllProjects());
    }
}
