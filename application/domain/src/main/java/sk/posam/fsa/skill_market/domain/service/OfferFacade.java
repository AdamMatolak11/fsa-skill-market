package sk.posam.fsa.skill_market.domain.service;

import sk.posam.fsa.skill_market.domain.offer.CreateOfferCommand;
import sk.posam.fsa.skill_market.domain.offer.Offer;

public interface OfferFacade {

    Offer createOffer(CreateOfferCommand command);
}
