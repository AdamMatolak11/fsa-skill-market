package sk.posam.fsa.skill_market.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import sk.posam.fsa.skill_market.domain.service.FreelancerFacade;
import sk.posam.fsa.skill_market.mapper.ProfileRestMapper;
import sk.posam.fsa.skill_market.rest.api.FreelancersApi;
import sk.posam.fsa.skill_market.rest.dto.FreelancerResponse;

@RestController
public class FreelancerRestController implements FreelancersApi {

    private final FreelancerFacade freelancerFacade;
    private final ProfileRestMapper profileRestMapper;

    public FreelancerRestController(FreelancerFacade freelancerFacade, ProfileRestMapper profileRestMapper) {
        this.freelancerFacade = freelancerFacade;
        this.profileRestMapper = profileRestMapper;
    }

    @Override
    public ResponseEntity<List<FreelancerResponse>> searchFreelancers(String skill) {
        List<FreelancerResponse> body = freelancerFacade.searchFreelancers(skill).stream()
                .map(profileRestMapper::toFreelancerResponse)
                .toList();
        return ResponseEntity.ok(body);
    }
}
