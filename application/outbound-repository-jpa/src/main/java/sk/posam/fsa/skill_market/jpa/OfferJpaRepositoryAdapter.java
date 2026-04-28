package sk.posam.fsa.skill_market.jpa;

import java.util.Optional;
import java.util.UUID;
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
    public Optional<Offer> findById(UUID offerId) {
        return offerSpringDataRepository.findById(offerId)
                .map(offerJpaMapper::toDomain);
    }

    @Override
    public java.util.List<Offer> findByProjectId(UUID projectId) {
        return offerSpringDataRepository.findByProjectIdOrderByCreatedAtDesc(projectId).stream()
                .map(offerJpaMapper::toDomain)
                .toList();
    }

    @Override
    public Offer save(Offer offer) {
        return offerJpaMapper.toDomain(offerSpringDataRepository.save(offerJpaMapper.toEntity(offer)));
    }

    @Override
    public java.util.List<Offer> saveAll(java.util.List<Offer> offers) {
        return offerSpringDataRepository.saveAll(offers.stream()
                        .map(offerJpaMapper::toEntity)
                        .toList()).stream()
                .map(offerJpaMapper::toDomain)
                .toList();
    }
}
