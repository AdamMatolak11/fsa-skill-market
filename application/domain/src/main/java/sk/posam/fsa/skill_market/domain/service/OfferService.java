package sk.posam.fsa.skill_market.domain.service;

import java.time.OffsetDateTime;
import sk.posam.fsa.skill_market.domain.offer.CreateOfferCommand;
import sk.posam.fsa.skill_market.domain.offer.Offer;
import sk.posam.fsa.skill_market.domain.offer.OfferCommandRepository;
import sk.posam.fsa.skill_market.domain.profile.UserProfile;
import sk.posam.fsa.skill_market.domain.profile.UserProfileNotFoundException;
import sk.posam.fsa.skill_market.domain.profile.UserProfileNotFreelancerException;
import sk.posam.fsa.skill_market.domain.profile.UserProfileQueryRepository;
import sk.posam.fsa.skill_market.domain.project.Project;
import sk.posam.fsa.skill_market.domain.project.ProjectNotAcceptingOffersException;
import sk.posam.fsa.skill_market.domain.project.ProjectNotFoundException;
import sk.posam.fsa.skill_market.domain.project.ProjectQueryRepository;

public class OfferService implements OfferFacade {

    private final ProjectQueryRepository projectQueryRepository;
    private final UserProfileQueryRepository userProfileQueryRepository;
    private final OfferCommandRepository offerCommandRepository;

    public OfferService(
            ProjectQueryRepository projectQueryRepository,
            UserProfileQueryRepository userProfileQueryRepository,
            OfferCommandRepository offerCommandRepository
    ) {
        this.projectQueryRepository = projectQueryRepository;
        this.userProfileQueryRepository = userProfileQueryRepository;
        this.offerCommandRepository = offerCommandRepository;
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
}
