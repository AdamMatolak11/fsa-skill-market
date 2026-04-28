package sk.posam.fsa.skill_market.domain.project;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProjectTest {

    @Test
    void restore_rejects_blank_title() {
        assertThrows(IllegalArgumentException.class, () -> Project.restore(
                UUID.randomUUID(),
                null,
                null,
                "   ",
                "Valid description",
                BigDecimal.TEN,
                "OPEN",
                OffsetDateTime.now()
        ));
    }

    @Test
    void open_project_is_visible_and_accepts_offers() {
        Project project = Project.restore(
                UUID.randomUUID(),
                UUID.randomUUID(),
                null,
                "API redesign",
                "Create new project API",
                BigDecimal.valueOf(1500),
                "OPEN",
                OffsetDateTime.now()
        );

        assertTrue(project.isVisibleInMarketplace());
        assertTrue(project.acceptsOffers());
    }

    @Test
    void in_progress_project_is_not_visible_in_marketplace() {
        Project project = Project.restore(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Mobile app",
                "Deliver first beta",
                BigDecimal.valueOf(5000),
                "IN_PROGRESS",
                OffsetDateTime.now()
        );

        assertFalse(project.isVisibleInMarketplace());
        assertFalse(project.acceptsOffers());
    }
}
