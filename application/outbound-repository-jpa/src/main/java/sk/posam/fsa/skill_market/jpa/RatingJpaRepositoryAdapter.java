package sk.posam.fsa.skill_market.jpa;

import org.springframework.stereotype.Repository;
import sk.posam.fsa.skill_market.domain.rating.Rating;
import sk.posam.fsa.skill_market.domain.rating.RatingCommandRepository;

@Repository
public class RatingJpaRepositoryAdapter implements RatingCommandRepository {

    private final RatingSpringDataRepository ratingSpringDataRepository;
    private final RatingJpaMapper ratingJpaMapper;

    public RatingJpaRepositoryAdapter(
            RatingSpringDataRepository ratingSpringDataRepository,
            RatingJpaMapper ratingJpaMapper
    ) {
        this.ratingSpringDataRepository = ratingSpringDataRepository;
        this.ratingJpaMapper = ratingJpaMapper;
    }

    @Override
    public Rating save(Rating rating) {
        ratingSpringDataRepository.save(ratingJpaMapper.toEntity(rating));
        return rating;
    }
}
