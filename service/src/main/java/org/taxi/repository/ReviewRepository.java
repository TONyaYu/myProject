package org.taxi.repository;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.taxi.entity.Review;

@Repository
public class ReviewRepository extends RepositoryBase<Long, Review> {

    public ReviewRepository(EntityManager entityManager) {
        super(Review.class, entityManager);
    }
}
