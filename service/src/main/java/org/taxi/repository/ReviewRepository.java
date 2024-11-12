package org.taxi.repository;

import jakarta.persistence.EntityManager;
import org.taxi.entity.Review;

public class ReviewRepository extends RepositoryBase<Long, Review> {

    public ReviewRepository(EntityManager entityManager) {
        super(Review.class, entityManager);
    }
}
