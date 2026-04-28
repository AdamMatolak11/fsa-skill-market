package sk.posam.fsa.skill_market;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sk.posam.fsa.skill_market.domain.offer.OfferCommandRepository;
import sk.posam.fsa.skill_market.domain.profile.UserProfileCommandRepository;
import sk.posam.fsa.skill_market.domain.profile.UserProfileQueryRepository;
import sk.posam.fsa.skill_market.domain.project.ProjectCommandRepository;
import sk.posam.fsa.skill_market.domain.project.ProjectQueryRepository;
import sk.posam.fsa.skill_market.domain.rating.RatingCommandRepository;
import sk.posam.fsa.skill_market.domain.service.FreelancerFacade;
import sk.posam.fsa.skill_market.domain.service.FreelancerService;
import sk.posam.fsa.skill_market.domain.service.OfferFacade;
import sk.posam.fsa.skill_market.domain.service.OfferService;
import sk.posam.fsa.skill_market.domain.service.ProfileFacade;
import sk.posam.fsa.skill_market.domain.service.ProfileService;
import sk.posam.fsa.skill_market.domain.service.ProjectFacade;
import sk.posam.fsa.skill_market.domain.service.ProjectService;
import sk.posam.fsa.skill_market.domain.service.RatingFacade;
import sk.posam.fsa.skill_market.domain.service.RatingService;

@Configuration
public class ProjectBeanConfiguration {

    @Bean
    public ProjectFacade projectFacade(
            ProjectQueryRepository projectQueryRepository,
            ProjectCommandRepository projectCommandRepository
    ) {
        return new ProjectService(projectQueryRepository, projectCommandRepository);
    }

    @Bean
    public FreelancerFacade freelancerFacade(UserProfileQueryRepository userProfileQueryRepository) {
        return new FreelancerService(userProfileQueryRepository);
    }

    @Bean
    public ProfileFacade profileFacade(
            UserProfileQueryRepository userProfileQueryRepository,
            UserProfileCommandRepository userProfileCommandRepository
    ) {
        return new ProfileService(userProfileQueryRepository, userProfileCommandRepository);
    }

    @Bean
    public OfferFacade offerFacade(
            ProjectQueryRepository projectQueryRepository,
            ProjectCommandRepository projectCommandRepository,
            UserProfileQueryRepository userProfileQueryRepository,
            OfferCommandRepository offerCommandRepository
    ) {
        return new OfferService(
                projectQueryRepository,
                projectCommandRepository,
                userProfileQueryRepository,
                offerCommandRepository
        );
    }

    @Bean
    public RatingFacade ratingFacade(
            ProjectQueryRepository projectQueryRepository,
            UserProfileQueryRepository userProfileQueryRepository,
            UserProfileCommandRepository userProfileCommandRepository,
            RatingCommandRepository ratingCommandRepository
    ) {
        return new RatingService(
                projectQueryRepository,
                userProfileQueryRepository,
                userProfileCommandRepository,
                ratingCommandRepository
        );
    }
}
