package sk.posam.fsa.skill_market.domain.offer;

import java.util.UUID;

public class OfferCannotBeDecidedException extends RuntimeException {

    public OfferCannotBeDecidedException(UUID offerId, OfferStatus status) {
        super("Offer with id '" + offerId + "' is in status '" + status + "' and cannot be decided");
    }
}
