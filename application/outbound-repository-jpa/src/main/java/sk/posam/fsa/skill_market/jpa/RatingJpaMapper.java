package sk.posam.fsa.skill_market.jpa;

import org.springframework.stereotype.Component;
import sk.posam.fsa.skill_market.domain.rating.Rating;

@Component
public class RatingJpaMapper {

    public Rating toDomain(RatingJpaEntity entity) {
        return Rating.restore(
                entity.getId(),
                entity.getProjectId(),
                entity.getClientId(),
                entity.getFreelancerId(),
                entity.getScore(),
                entity.getComment(),
                entity.getCreatedAt()
        );
    }

    public RatingJpaEntity toEntity(Rating rating) {
        RatingJpaEntity entity = new RatingJpaEntity();
        entity.setId(rating.id());
        entity.setProjectId(rating.projectId());
        entity.setClientId(rating.clientId());
        entity.setFreelancerId(rating.freelancerId());
        entity.setScore(rating.score());
        entity.setComment(rating.comment());
        entity.setCreatedAt(rating.createdAt());
        return entity;
    }
}
