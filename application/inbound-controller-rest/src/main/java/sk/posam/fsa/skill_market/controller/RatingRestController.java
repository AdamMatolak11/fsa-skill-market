package sk.posam.fsa.skill_market.controller;

import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import sk.posam.fsa.skill_market.domain.service.RatingFacade;
import sk.posam.fsa.skill_market.mapper.RatingRestMapper;
import sk.posam.fsa.skill_market.rest.api.RatingsApi;
import sk.posam.fsa.skill_market.rest.dto.CreateRatingRequest;
import sk.posam.fsa.skill_market.rest.dto.RatingResponse;

@RestController
public class RatingRestController implements RatingsApi {

    private final RatingFacade ratingFacade;
    private final RatingRestMapper ratingRestMapper;

    public RatingRestController(RatingFacade ratingFacade, RatingRestMapper ratingRestMapper) {
        this.ratingFacade = ratingFacade;
        this.ratingRestMapper = ratingRestMapper;
    }

    @Override
    public ResponseEntity<RatingResponse> createRating(UUID projectId, CreateRatingRequest createRatingRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ratingRestMapper.toResponse(
                        ratingFacade.createRating(ratingRestMapper.toCommand(projectId, createRatingRequest))
                ));
    }
}
