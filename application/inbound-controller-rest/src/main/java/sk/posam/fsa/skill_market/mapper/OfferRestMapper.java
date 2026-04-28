package sk.posam.fsa.skill_market.mapper;

import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.stereotype.Component;
import sk.posam.fsa.skill_market.domain.offer.CreateOfferCommand;
import sk.posam.fsa.skill_market.domain.offer.Offer;
import sk.posam.fsa.skill_market.rest.dto.CreateOfferRequest;
import sk.posam.fsa.skill_market.rest.dto.OfferResponse;

@Component
public class OfferRestMapper {

    public CreateOfferCommand toCommand(UUID projectId, CreateOfferRequest request) {
        return new CreateOfferCommand(
                projectId,
                request.getFreelancerId(),
                BigDecimal.valueOf(request.getAmount()),
                request.getMessage()
        );
    }

    public OfferResponse toResponse(Offer offer) {
        OfferResponse response = new OfferResponse();
        response.setId(offer.id());
        response.setProjectId(offer.projectId());
        response.setFreelancerId(offer.freelancerId());
        response.setAmount(offer.amount().doubleValue());
        response.setMessage(offer.message());
        response.setStatus(OfferResponse.StatusEnum.fromValue(offer.status().name()));
        response.setCreatedAt(offer.createdAt());
        return response;
    }
}
