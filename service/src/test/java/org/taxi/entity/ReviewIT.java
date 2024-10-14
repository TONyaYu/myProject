package org.taxi.entity;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.taxi.util.HibernateTestUtil;
import org.taxi.util.TestObjectsUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


class ReviewIT {
    private static SessionFactory sessionFactory;
    private Session session;
    @BeforeAll
    static void createSessionFactory() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
    }

    @BeforeEach
    void  openSession() {
        session = sessionFactory.openSession();
        session.beginTransaction();
    }

    @AfterEach
    void  closeSession() {
        session.getTransaction().rollback();
        session.close();
    }

    @AfterAll
    static void closeSessionFactory() {
        sessionFactory.close();
    }

    @Test
    void createReview() {
        User client = TestObjectsUtils.getClient("Ivan", "Ivanov2@gmail.com");
        Ride ride = TestObjectsUtils.getRide("work", "home");
        Review review = TestObjectsUtils.getReview(ride, client, "Excellent service");

        session.save(review);
        session.clear();

        Assertions.assertNotNull(review.getId());
    }

    @Test
    void findByID() {
        User client1 = TestObjectsUtils.getClient("Max", "Ivanov3@gmail.com");
        Ride ride1 = TestObjectsUtils.getRide("work", "home");
        Review review1 = TestObjectsUtils.getReview(ride1, client1, "Excellent service");
        User client2 = TestObjectsUtils.getClient("Ivan", "Ivanov4@gmail.com");
        Ride ride2 = TestObjectsUtils.getRide("home", "work");
        Review review2 = TestObjectsUtils.getReview(ride2, client2, "Not bad");
        User client3 = TestObjectsUtils.getClient("Vadim", "Vadim@gmail.com");
        Ride ride3 = TestObjectsUtils.getRide("work", "aeroport");
        Review review3 = TestObjectsUtils.getReview(ride3, client3, "It`s OK");
        session.persist(review1);
        session.persist(review2);
        session.persist(review3);
        session.clear();

        Review actualReview = session.find(Review.class, review3.getId());

        assertEquals(review3.getId(), actualReview.getId());
    }

    @Test
    void updateReview() {
        User client = TestObjectsUtils.getClient("Ivan", "Ivanov2@gmail.com");
        Ride ride = TestObjectsUtils.getRide("work", "home");
        Review review = TestObjectsUtils.getReview(ride, client, "Excellent service");
        session.persist(review);
        session.clear();

        review.setRating(4);
        session.update(review);

        Review actualReview = session.find(Review.class, review.getId());
        assertEquals(review.getRating(), actualReview.getRating());
    }

    @Test
    void deleteReview() {
        User client = TestObjectsUtils.getClient("Max", "Ivanov1@gmail.com");
        Ride ride = TestObjectsUtils.getRide("work", "home");
        Review review = TestObjectsUtils.getReview(ride, client, "Excellent service");
        session.persist(review);
        session.clear();

        session.remove(review);
        Review actualReview = session.find(Review.class, review.getId());

        assertNull(actualReview);
    }
}