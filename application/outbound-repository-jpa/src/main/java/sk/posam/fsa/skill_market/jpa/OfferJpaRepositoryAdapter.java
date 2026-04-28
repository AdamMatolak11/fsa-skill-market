package sk.posam.fsa.skill_market.jpa;

import org.springframework.stereotype.Repository;
import sk.posam.fsa.skill_market.domain.offer.Offer;
import sk.posam.fsa.skill_market.domain.offer.OfferCommandRepository;

@Repository
public class OfferJpaRepositoryAdapter implements OfferCommandRepository {

    private final OfferSpringDataRepository offerSpringDataRepository;
    private final OfferJpaMapper offerJpaMapper;

    public OfferJpaRepositoryAdapter(
            OfferSpringDataRepository offerSpringDataRepository,
            OfferJpaMapper offerJpaMapper
    ) {
        this.offerSpringDataRepository = offerSpringDataRepository;
        this.offerJpaMapper = offerJpaMapper;
    }

    @Override
    public Offer save(Offer offer) {
        return offerJpaMapper.toDomain(offerSpringDataRepository.save(offerJpaMapper.toEntity(offer)));
    }
}
