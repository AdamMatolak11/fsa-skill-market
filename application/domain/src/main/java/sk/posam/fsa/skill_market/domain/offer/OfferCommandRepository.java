package sk.posam.fsa.skill_market.domain.offer;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

public interface OfferCommandRepository {

    Optional<Offer> findById(UUID offerId);

    List<Offer> findByProjectId(UUID projectId);

    Offer save(Offer offer);

    List<Offer> saveAll(List<Offer> offers);

}
