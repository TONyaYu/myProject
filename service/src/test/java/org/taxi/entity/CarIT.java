package org.taxi.entity;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.taxi.util.HibernateTestUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CarIT {

    private static SessionFactory sessionFactory;
    private Session session;

    @BeforeAll
    static void createSessionFactory() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
    }

    @BeforeEach
    void openSession() {
        session = sessionFactory.openSession();
        session.beginTransaction();
    }

    @AfterEach
    void closeSession() {
        session.getTransaction().rollback();
        session.close();
    }

    @AfterAll
    static void closeSessionFactory() {
        sessionFactory.close();
    }

    @Test
    void createCar() {
        Car car = Car.builder()
                .make("BMW")
                .model("x5")
                .licensePlate("125gf684")
                .isAvailable(true)
                .build();
        session.persist(car);
        session.clear();

        assertNotNull(car.getId());
    }

    @Test
    void updateCar() {
        Car car = Car.builder()
                .make("BMW")
                .model("x5")
                .licensePlate("125gf684")
                .isAvailable(true)
                .build();
        session.persist(car);
        session.clear();

        car.setAvailable(false);
        session.update(car);
        Car actualCar = session.get(Car.class, 1);

        assertEquals(car.isAvailable(), actualCar.isAvailable());
    }
}