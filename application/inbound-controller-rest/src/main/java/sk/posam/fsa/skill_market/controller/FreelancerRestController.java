package sk.posam.fsa.skill_market.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import sk.posam.fsa.skill_market.domain.service.FreelancerFacade;
import sk.posam.fsa.skill_market.mapper.ProfileRestMapper;
import sk.posam.fsa.skill_market.rest.api.FreelancersApi;
import sk.posam.fsa.skill_market.rest.dto.FreelancerResponse;
import sk.posam.fsa.skill_market.security.AuthenticatedUserProvider;

@RestController
public class FreelancerRestController implements FreelancersApi {

    private final FreelancerFacade freelancerFacade;
    private final ProfileRestMapper profileRestMapper;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    public FreelancerRestController(
            FreelancerFacade freelancerFacade,
            ProfileRestMapper profileRestMapper,
            AuthenticatedUserProvider authenticatedUserProvider
    ) {
        this.freelancerFacade = freelancerFacade;
        this.profileRestMapper = profileRestMapper;
        this.authenticatedUserProvider = authenticatedUserProvider;
    }

    @Override
    public ResponseEntity<List<FreelancerResponse>> searchFreelancers(String skill) {
        authenticatedUserProvider.currentUser(); // Trigger JIT provisioning
        List<FreelancerResponse> body = freelancerFacade.searchFreelancers(skill).stream()
                .map(profileRestMapper::toFreelancerResponse)
                .toList();
        return ResponseEntity.ok(body);
    }
}
