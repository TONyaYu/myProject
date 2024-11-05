package org.taxi.dao;

import org.hibernate.SessionFactory;
import org.taxi.entity.Review;

import static org.taxi.entity.QReview.review;

public class ReviewRepository extends RepositoryBase<Long, Review> {

    public ReviewRepository(SessionFactory sessionFactory) {
        super(Review.class, sessionFactory, review);
    }
}
