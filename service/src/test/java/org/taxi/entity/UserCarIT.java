package org.taxi.entity;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.taxi.util.HibernateTestUtil;
import org.taxi.util.TestObjectsUtils;

class UserCarIT {
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
    void check() {
User user = session.get(User.class, 1L);
//        User user = TestObjectsUtils.getDriver("Nik", "123@mail.ru");
        Car car = TestObjectsUtils.getCar("7", "123es29");

        UserCar userCar = UserCar.builder()
                .build();
        userCar.setUser(user);
        userCar.setCar(car);
        session.clear();
        session.persist(userCar);
    }
}