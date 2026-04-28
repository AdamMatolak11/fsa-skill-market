package sk.posam.fsa.skill_market.mapper;

import java.util.UUID;
import org.springframework.stereotype.Component;
import sk.posam.fsa.skill_market.domain.rating.CreateRatingCommand;
import sk.posam.fsa.skill_market.domain.rating.Rating;
import sk.posam.fsa.skill_market.rest.dto.CreateRatingRequest;
import sk.posam.fsa.skill_market.rest.dto.RatingResponse;

@Component
public class RatingRestMapper {

    public CreateRatingCommand toCommand(UUID projectId, CreateRatingRequest request) {
        return new CreateRatingCommand(
                projectId,
                request.getClientId(),
                request.getFreelancerId(),
                request.getScore(),
                request.getComment()
        );
    }

    public RatingResponse toResponse(Rating rating) {
        RatingResponse response = new RatingResponse();
        response.setId(rating.id());
        response.setProjectId(rating.projectId());
        response.setClientId(rating.clientId());
        response.setFreelancerId(rating.freelancerId());
        response.setScore(rating.score());
        response.setComment(rating.comment());
        response.setCreatedAt(rating.createdAt());
        return response;
    }
}
