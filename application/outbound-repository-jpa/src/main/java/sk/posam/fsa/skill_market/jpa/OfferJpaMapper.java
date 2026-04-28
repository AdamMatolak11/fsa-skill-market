package sk.posam.fsa.skill_market.jpa;

import org.springframework.stereotype.Component;
import sk.posam.fsa.skill_market.domain.offer.Offer;

@Component
public class OfferJpaMapper {

    public Offer toDomain(OfferJpaEntity entity) {
        return Offer.restore(
                entity.getId(),
                entity.getProjectId(),
                entity.getFreelancerId(),
                entity.getAmount(),
                entity.getMessage(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }

    public OfferJpaEntity toEntity(Offer offer) {
        OfferJpaEntity entity = new OfferJpaEntity();
        entity.setId(offer.id());
        entity.setProjectId(offer.projectId());
        entity.setFreelancerId(offer.freelancerId());
        entity.setAmount(offer.amount());
        entity.setMessage(offer.message());
        entity.setStatus(offer.status().name());
        entity.setCreatedAt(offer.createdAt());
        return entity;
    }
}
