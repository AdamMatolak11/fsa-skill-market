package sk.posam.fsa.skill_market.domain.service;

import java.time.OffsetDateTime;
import sk.posam.fsa.skill_market.domain.profile.UserProfile;
import sk.posam.fsa.skill_market.domain.profile.UserProfileCommandRepository;
import sk.posam.fsa.skill_market.domain.profile.UserProfileNotFoundException;
import sk.posam.fsa.skill_market.domain.profile.UserProfileNotFreelancerException;
import sk.posam.fsa.skill_market.domain.profile.UserProfileQueryRepository;
import sk.posam.fsa.skill_market.domain.project.Project;
import sk.posam.fsa.skill_market.domain.project.ProjectNotCompletedException;
import sk.posam.fsa.skill_market.domain.project.ProjectNotFoundException;
import sk.posam.fsa.skill_market.domain.project.ProjectQueryRepository;
import sk.posam.fsa.skill_market.domain.rating.CreateRatingCommand;
import sk.posam.fsa.skill_market.domain.rating.Rating;
import sk.posam.fsa.skill_market.domain.rating.RatingCommandRepository;

public class RatingService implements RatingFacade {

    private final ProjectQueryRepository projectQueryRepository;
    private final UserProfileQueryRepository userProfileQueryRepository;
    private final UserProfileCommandRepository userProfileCommandRepository;
    private final RatingCommandRepository ratingCommandRepository;

    public RatingService(
            ProjectQueryRepository projectQueryRepository,
            UserProfileQueryRepository userProfileQueryRepository,
            UserProfileCommandRepository userProfileCommandRepository,
            RatingCommandRepository ratingCommandRepository
    ) {
        this.projectQueryRepository = projectQueryRepository;
        this.userProfileQueryRepository = userProfileQueryRepository;
        this.userProfileCommandRepository = userProfileCommandRepository;
        this.ratingCommandRepository = ratingCommandRepository;
    }

    @Override
    public Rating createRating(CreateRatingCommand command) {
        Project project = projectQueryRepository.findById(command.projectId())
                .orElseThrow(() -> new ProjectNotFoundException(command.projectId()));
        if (!project.isCompleted()) {
            throw new ProjectNotCompletedException(project.id(), project.status());
        }

        userProfileQueryRepository.findById(command.clientId())
                .orElseThrow(() -> new UserProfileNotFoundException(command.clientId()));

        UserProfile freelancer = userProfileQueryRepository.findById(command.freelancerId())
                .orElseThrow(() -> new UserProfileNotFoundException(command.freelancerId()));
        if (!freelancer.isFreelancer()) {
            throw new UserProfileNotFreelancerException(freelancer.id());
        }

        Rating rating = Rating.createNew(
                command.projectId(),
                command.clientId(),
                command.freelancerId(),
                command.score(),
                command.comment(),
                OffsetDateTime.now()
        );
        userProfileCommandRepository.save(freelancer.withNewRating(command.score()));
        return ratingCommandRepository.save(rating);
    }
}
