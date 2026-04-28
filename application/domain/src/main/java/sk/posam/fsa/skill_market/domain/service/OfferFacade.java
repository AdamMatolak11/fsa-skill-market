package sk.posam.fsa.skill_market.domain.service;

import java.util.List;
import java.util.UUID;
import sk.posam.fsa.skill_market.domain.offer.CreateOfferCommand;
import sk.posam.fsa.skill_market.domain.offer.Offer;

public interface OfferFacade {

    List<Offer> getProjectOffers(UUID projectId);

    Offer createOffer(CreateOfferCommand command);

    Offer acceptOffer(UUID projectId, UUID offerId);

    Offer rejectOffer(UUID projectId, UUID offerId);

    void deleteOffer(UUID projectId, UUID offerId);
}
