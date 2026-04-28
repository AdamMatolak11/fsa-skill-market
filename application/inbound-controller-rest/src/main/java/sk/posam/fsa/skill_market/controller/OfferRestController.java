package sk.posam.fsa.skill_market.controller;

import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import sk.posam.fsa.skill_market.domain.service.OfferFacade;
import sk.posam.fsa.skill_market.mapper.OfferRestMapper;
import sk.posam.fsa.skill_market.rest.api.OffersApi;
import sk.posam.fsa.skill_market.rest.dto.CreateOfferRequest;
import sk.posam.fsa.skill_market.rest.dto.OfferResponse;

@RestController
public class OfferRestController implements OffersApi {

    private final OfferFacade offerFacade;
    private final OfferRestMapper offerRestMapper;

    public OfferRestController(OfferFacade offerFacade, OfferRestMapper offerRestMapper) {
        this.offerFacade = offerFacade;
        this.offerRestMapper = offerRestMapper;
    }

    @Override
    public ResponseEntity<OfferResponse> createOffer(UUID projectId, CreateOfferRequest createOfferRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(offerRestMapper.toResponse(
                        offerFacade.createOffer(offerRestMapper.toCommand(projectId, createOfferRequest))
                ));
    }
}
