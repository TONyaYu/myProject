package org.taxi.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.taxi.entity.User;
import org.taxi.entity.UserRole;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppTestIT {

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
    void checkUser() {
        User user = User.builder()
                .firstName("Max")
                .lastName("Ivanov")
                .email("max@gmail.com")
                .userRole(UserRole.DRIVER)
                .build();
        session.persist(user);
        session.flush();

        session.clear();
        User userActual = session.find(User.class, user.getId());

        assertEquals(user, userActual);
    }
}

