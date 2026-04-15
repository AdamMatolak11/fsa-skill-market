package sk.posam.fsa.skill_market;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sk.posam.fsa.skill_market.domain.project.ProjectCommandRepository;
import sk.posam.fsa.skill_market.domain.project.ProjectQueryRepository;
import sk.posam.fsa.skill_market.domain.service.ProjectFacade;
import sk.posam.fsa.skill_market.domain.service.ProjectService;

@Configuration
public class ProjectBeanConfiguration {

    @Bean
    public ProjectFacade projectFacade(
            ProjectQueryRepository projectQueryRepository,
            ProjectCommandRepository projectCommandRepository
    ) {
        return new ProjectService(projectQueryRepository, projectCommandRepository);
    }
}
