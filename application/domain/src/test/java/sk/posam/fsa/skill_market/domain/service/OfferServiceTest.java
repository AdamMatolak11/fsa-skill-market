package sk.posam.fsa.skill_market.domain.service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import sk.posam.fsa.skill_market.domain.offer.Offer;
import sk.posam.fsa.skill_market.domain.offer.OfferCannotBeCancelledException;
import sk.posam.fsa.skill_market.domain.offer.OfferCommandRepository;
import sk.posam.fsa.skill_market.domain.offer.OfferNotFoundException;
import sk.posam.fsa.skill_market.domain.profile.UserProfile;
import sk.posam.fsa.skill_market.domain.profile.UserProfileQueryRepository;
import sk.posam.fsa.skill_market.domain.project.Project;
import sk.posam.fsa.skill_market.domain.project.ProjectCommandRepository;
import sk.posam.fsa.skill_market.domain.project.ProjectNotFoundException;
import sk.posam.fsa.skill_market.domain.project.ProjectQueryRepository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OfferServiceTest {

    @Test
    void deleteOffer_marksPendingOfferAsCancelled() {
        UUID projectId = UUID.randomUUID();
        Offer existingOffer = Offer.restore(
                UUID.randomUUID(),
                projectId,
                UUID.randomUUID(),
                BigDecimal.valueOf(1200),
                "Can start immediately",
                "PENDING",
                OffsetDateTime.parse("2026-03-10T10:15:30+01:00")
        );
        InMemoryOfferCommandRepository offerCommandRepository = new InMemoryOfferCommandRepository(existingOffer);
        OfferService service = new OfferService(
                new SingleProjectQueryRepository(project(projectId)),
                new InMemoryProjectCommandRepository(),
                new EmptyUserProfileQueryRepository(),
                offerCommandRepository
        );

        service.deleteOffer(projectId, existingOffer.id());

        assertEquals("CANCELLED", offerCommandRepository.savedOffer.status().name());
    }

    @Test
    void deleteOffer_rejectsNonPendingOffer() {
        UUID projectId = UUID.randomUUID();
        Offer existingOffer = Offer.restore(
                UUID.randomUUID(),
                projectId,
                UUID.randomUUID(),
                BigDecimal.valueOf(1200),
                "Already negotiated",
                "ACCEPTED",
                OffsetDateTime.parse("2026-03-10T10:15:30+01:00")
        );
        InMemoryOfferCommandRepository offerCommandRepository = new InMemoryOfferCommandRepository(existingOffer);
        OfferService service = new OfferService(
                new SingleProjectQueryRepository(project(projectId)),
                new InMemoryProjectCommandRepository(),
                new EmptyUserProfileQueryRepository(),
                offerCommandRepository
        );

        assertThrows(OfferCannotBeCancelledException.class, () -> service.deleteOffer(projectId, existingOffer.id()));
        assertTrue(offerCommandRepository.savedOffer == null);
    }

    @Test
    void deleteOffer_returnsNotFoundWhenOfferBelongsToDifferentProject() {
        UUID projectId = UUID.randomUUID();
        Offer existingOffer = Offer.restore(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                BigDecimal.valueOf(1200),
                "Wrong project",
                "PENDING",
                OffsetDateTime.parse("2026-03-10T10:15:30+01:00")
        );
        OfferService service = new OfferService(
                new SingleProjectQueryRepository(project(projectId)),
                new InMemoryProjectCommandRepository(),
                new EmptyUserProfileQueryRepository(),
                new InMemoryOfferCommandRepository(existingOffer)
        );

        assertThrows(OfferNotFoundException.class, () -> service.deleteOffer(projectId, existingOffer.id()));
    }

    @Test
    void deleteOffer_returnsNotFoundWhenProjectDoesNotExist() {
        OfferService service = new OfferService(
                new EmptyProjectQueryRepository(),
                new InMemoryProjectCommandRepository(),
                new EmptyUserProfileQueryRepository(),
                new InMemoryOfferCommandRepository(null)
        );

        assertThrows(ProjectNotFoundException.class, () -> service.deleteOffer(UUID.randomUUID(), UUID.randomUUID()));
    }

    private static Project project(UUID projectId) {
        return Project.restore(
                projectId,
                UUID.randomUUID(),
                null,
                "Spring Boot API",
                "Implement API endpoint for projects.",
                BigDecimal.valueOf(1500),
                "OPEN",
                OffsetDateTime.parse("2026-01-10T10:15:30+01:00")
        );
    }

    private static final class InMemoryOfferCommandRepository implements OfferCommandRepository {
        private final Offer existingOffer;
        private Offer savedOffer;

        private InMemoryOfferCommandRepository(Offer existingOffer) {
            this.existingOffer = existingOffer;
        }

        @Override
        public Optional<Offer> findById(UUID offerId) {
            if (existingOffer != null && existingOffer.id().equals(offerId)) {
                return Optional.of(existingOffer);
            }
            return Optional.empty();
        }

        @Override
        public List<Offer> findByProjectId(UUID projectId) {
            return existingOffer != null && existingOffer.projectId().equals(projectId)
                    ? List.of(existingOffer)
                    : List.of();
        }

        @Override
        public Offer save(Offer offer) {
            this.savedOffer = offer;
            return offer;
        }

        @Override
        public List<Offer> saveAll(List<Offer> offers) {
            return offers;
        }
    }

    private static final class InMemoryProjectCommandRepository implements ProjectCommandRepository {
        @Override
        public boolean existsByTitle(String title) {
            return false;
        }

        @Override
        public Project save(Project project) {
            return project;
        }
    }

    private static final class SingleProjectQueryRepository implements ProjectQueryRepository {
        private final Project project;

        private SingleProjectQueryRepository(Project project) {
            this.project = project;
        }

        @Override
        public List<Project> findAll() {
            return List.of(project);
        }

        @Override
        public Optional<Project> findById(UUID projectId) {
            return project.id().equals(projectId) ? Optional.of(project) : Optional.empty();
        }
    }

    private static final class EmptyProjectQueryRepository implements ProjectQueryRepository {
        @Override
        public List<Project> findAll() {
            return List.of();
        }

        @Override
        public Optional<Project> findById(UUID projectId) {
            return Optional.empty();
        }
    }

    private static final class EmptyUserProfileQueryRepository implements UserProfileQueryRepository {
        @Override
        public List<UserProfile> findAll() {
            return List.of();
        }

        @Override
        public Optional<UserProfile> findById(UUID userId) {
            return Optional.empty();
        }

        @Override
        public Optional<UserProfile> findByEmail(String email) {
            return Optional.empty();
        }
    }
}
