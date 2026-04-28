package sk.posam.fsa.skill_market.domain.service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import sk.posam.fsa.skill_market.domain.offer.CreateOfferCommand;
import sk.posam.fsa.skill_market.domain.offer.Offer;
import sk.posam.fsa.skill_market.domain.offer.OfferCannotBeCancelledException;
import sk.posam.fsa.skill_market.domain.offer.OfferCannotBeDecidedException;
import sk.posam.fsa.skill_market.domain.offer.OfferCommandRepository;
import sk.posam.fsa.skill_market.domain.offer.OfferNotFoundException;
import sk.posam.fsa.skill_market.domain.profile.UserProfile;
import sk.posam.fsa.skill_market.domain.profile.UserProfileNotFoundException;
import sk.posam.fsa.skill_market.domain.profile.UserProfileNotFreelancerException;
import sk.posam.fsa.skill_market.domain.profile.UserProfileQueryRepository;
import sk.posam.fsa.skill_market.domain.project.Project;
import sk.posam.fsa.skill_market.domain.project.ProjectCommandRepository;
import sk.posam.fsa.skill_market.domain.project.ProjectNotAcceptingOffersException;
import sk.posam.fsa.skill_market.domain.project.ProjectNotFoundException;
import sk.posam.fsa.skill_market.domain.project.ProjectQueryRepository;

public class OfferService implements OfferFacade {

    private final ProjectQueryRepository projectQueryRepository;
    private final ProjectCommandRepository projectCommandRepository;
    private final UserProfileQueryRepository userProfileQueryRepository;
    private final OfferCommandRepository offerCommandRepository;

    public OfferService(
            ProjectQueryRepository projectQueryRepository,
            ProjectCommandRepository projectCommandRepository,
            UserProfileQueryRepository userProfileQueryRepository,
            OfferCommandRepository offerCommandRepository
    ) {
        this.projectQueryRepository = projectQueryRepository;
        this.projectCommandRepository = projectCommandRepository;
        this.userProfileQueryRepository = userProfileQueryRepository;
        this.offerCommandRepository = offerCommandRepository;
    }

    @Override
    public List<Offer> getProjectOffers(UUID projectId) {
        projectQueryRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
        return offerCommandRepository.findByProjectId(projectId);
    }

    @Override
    public Offer createOffer(CreateOfferCommand command) {
        Project project = projectQueryRepository.findById(command.projectId())
                .orElseThrow(() -> new ProjectNotFoundException(command.projectId()));
        if (!project.acceptsOffers()) {
            throw new ProjectNotAcceptingOffersException(project.id(), project.status());
        }

        UserProfile freelancer = userProfileQueryRepository.findById(command.freelancerId())
                .orElseThrow(() -> new UserProfileNotFoundException(command.freelancerId()));
        if (!freelancer.isFreelancer()) {
            throw new UserProfileNotFreelancerException(freelancer.id());
        }

        Offer offer = Offer.createNew(
                command.projectId(),
                command.freelancerId(),
                command.amount(),
                command.message(),
                OffsetDateTime.now()
        );
        return offerCommandRepository.save(offer);
    }

    @Override
    public Offer acceptOffer(UUID projectId, UUID offerId) {
        Project project = projectQueryRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
        Offer offer = getOfferForProject(projectId, offerId);
        if (!offer.canBeDecided()) {
            throw new OfferCannotBeDecidedException(offer.id(), offer.status());
        }

        Offer acceptedOffer = offer.accept();
        List<Offer> updatedOffers = new ArrayList<>();
        for (Offer projectOffer : offerCommandRepository.findByProjectId(projectId)) {
            if (projectOffer.id().equals(offerId)) {
                updatedOffers.add(acceptedOffer);
            } else if (projectOffer.canBeDecided()) {
                updatedOffers.add(projectOffer.reject());
            } else {
                updatedOffers.add(projectOffer);
            }
        }
        offerCommandRepository.saveAll(updatedOffers);
        projectCommandRepository.save(project.start(acceptedOffer.freelancerId()));
        return acceptedOffer;
    }

    @Override
    public Offer rejectOffer(UUID projectId, UUID offerId) {
        projectQueryRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
        Offer offer = getOfferForProject(projectId, offerId);
        if (!offer.canBeDecided()) {
            throw new OfferCannotBeDecidedException(offer.id(), offer.status());
        }
        return offerCommandRepository.save(offer.reject());
    }

    @Override
    public void deleteOffer(UUID projectId, UUID offerId) {
        projectQueryRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
        Offer offer = getOfferForProject(projectId, offerId);
        if (!offer.canBeCancelled()) {
            throw new OfferCannotBeCancelledException(offer.id(), offer.status());
        }
        offerCommandRepository.save(offer.cancel());
    }

    private Offer getOfferForProject(UUID projectId, UUID offerId) {
        Offer offer = offerCommandRepository.findById(offerId)
                .orElseThrow(() -> new OfferNotFoundException(offerId));
        if (!offer.projectId().equals(projectId)) {
            throw new OfferNotFoundException(offerId);
        }
        return offer;
    }
}
