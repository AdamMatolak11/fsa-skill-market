package sk.posam.fsa.skill_market.domain.service;

import sk.posam.fsa.skill_market.domain.rating.CreateRatingCommand;
import sk.posam.fsa.skill_market.domain.rating.Rating;

public interface RatingFacade {

    Rating createRating(CreateRatingCommand command);
}
