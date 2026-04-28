package sk.posam.fsa.skill_market.domain.offer;

import java.util.UUID;

public class OfferCannotBeCancelledException extends RuntimeException {

    public OfferCannotBeCancelledException(UUID offerId, OfferStatus status) {
        super("Offer with id '" + offerId + "' is in status '" + status + "' and cannot be cancelled");
    }
}
