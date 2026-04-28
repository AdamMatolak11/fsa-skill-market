package sk.posam.fsa.skill_market.domain.offer;

import java.util.UUID;

public class OfferNotFoundException extends RuntimeException {

    public OfferNotFoundException(UUID offerId) {
        super("Offer with id '" + offerId + "' was not found");
    }
}
