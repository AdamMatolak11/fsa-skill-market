package sk.posam.fsa.skill_market.domain.service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import sk.posam.fsa.skill_market.domain.project.CreateProjectCommand;
import sk.posam.fsa.skill_market.domain.project.Project;
import sk.posam.fsa.skill_market.domain.project.ProjectAlreadyExistsException;
import sk.posam.fsa.skill_market.domain.project.ProjectCommandRepository;
import sk.posam.fsa.skill_market.domain.project.ProjectQueryRepository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

        ProjectCommandRepository commandRepository = new InMemoryProjectCommandRepository();

        ProjectService service = new ProjectService(repository, commandRepository);

        assertEquals(List.of(newerOpenProject, olderOpenProject), service.getAllProjects());
    }

    @Test
    void createProject_setsOpenStatusAndPersistsProject() {
        InMemoryProjectCommandRepository commandRepository = new InMemoryProjectCommandRepository();
        ProjectService service = new ProjectService(List::of, commandRepository);

        Project createdProject = service.createProject(new CreateProjectCommand(
                "Client portal",
                "Create a new customer portal",
                BigDecimal.valueOf(2500)
        ));

        assertEquals("Client portal", createdProject.title());
        assertEquals("OPEN", createdProject.status().name());
        assertTrue(commandRepository.savedProject != null);
    }

    @Test
    void createProject_rejectsDuplicateTitle() {
        InMemoryProjectCommandRepository commandRepository = new InMemoryProjectCommandRepository();
        commandRepository.existingTitle = "Existing Project";
        ProjectService service = new ProjectService(List::of, commandRepository);

        assertThrows(ProjectAlreadyExistsException.class, () -> service.createProject(new CreateProjectCommand(
                "Existing Project",
                "Duplicate",
                BigDecimal.valueOf(1000)
        )));
    }

    private static final class InMemoryProjectCommandRepository implements ProjectCommandRepository {
        private String existingTitle;
        private Project savedProject;

        @Override
        public boolean existsByTitle(String title) {
            return existingTitle != null && existingTitle.equalsIgnoreCase(title);
        }

        @Override
        public Project save(Project project) {
            this.savedProject = project;
            return project;
        }
    }
}
