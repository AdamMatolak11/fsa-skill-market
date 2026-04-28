package sk.posam.fsa.skill_market.controller;

import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import sk.posam.fsa.skill_market.domain.service.RatingFacade;
import sk.posam.fsa.skill_market.mapper.RatingRestMapper;
import sk.posam.fsa.skill_market.rest.api.RatingsApi;
import sk.posam.fsa.skill_market.rest.dto.CreateRatingRequest;
import sk.posam.fsa.skill_market.rest.dto.RatingResponse;
import sk.posam.fsa.skill_market.security.AuthenticatedUser;
import sk.posam.fsa.skill_market.security.AuthenticatedUserProvider;
import sk.posam.fsa.skill_market.security.ForbiddenOperationException;

@RestController
public class RatingRestController implements RatingsApi {

    private final RatingFacade ratingFacade;
    private final RatingRestMapper ratingRestMapper;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    public RatingRestController(
            RatingFacade ratingFacade,
            RatingRestMapper ratingRestMapper,
            AuthenticatedUserProvider authenticatedUserProvider
    ) {
        this.ratingFacade = ratingFacade;
        this.ratingRestMapper = ratingRestMapper;
        this.authenticatedUserProvider = authenticatedUserProvider;
    }

    @Override
    public ResponseEntity<RatingResponse> createRating(UUID projectId, CreateRatingRequest createRatingRequest) {
        assertCanCreateRating(createRatingRequest.getClientId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ratingRestMapper.toResponse(
                        ratingFacade.createRating(ratingRestMapper.toCommand(projectId, createRatingRequest))
                ));
    }

    private void assertCanCreateRating(UUID clientId) {
        Optional<AuthenticatedUser> currentUser = authenticatedUserProvider.currentUser();
        if (currentUser.isEmpty()) {
            return;
        }

        AuthenticatedUser user = currentUser.get();
        if (!user.hasRole("ADMIN") && !user.userId().equals(clientId)) {
            throw new ForbiddenOperationException("Current user cannot create ratings for client '" + clientId + "'");
        }
    }
}
