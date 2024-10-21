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
import static org.junit.jupiter.api.Assertions.*;

class UserIT {

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
    void createUser() {
        Ride ride = TestObjectsUtils.getRide("aero", "home");
        User driver = User.builder()
                .firstName("Max")
                .lastName("Ivanov")
                .email("Max@gmail.com")
                .password("client1")
                .phone("16379872")
                .userRole(UserRole.DRIVER)
                .build();

        session.save(ride);
        session.save(driver);
        session.clear();

        Assertions.assertNotNull(driver.getId());
    }

    @Test
    void findUser() {
        User user = TestObjectsUtils.getClient("Maria", "masha@medved.com");
        User user1 = TestObjectsUtils.getDriver("Alex", "vol@gmail.com");
        User user2 = TestObjectsUtils.getDriver("Lev", "lev@gmail.com");

        session.persist(user);
        session.persist(user1);
        session.persist(user2);
        session.clear();

        User actualUser = session.find(User.class, user1.getId());

        assertEquals(user1.getId(), actualUser.getId());
    }

    @Test
    void updateUser() {
        User driver = User.builder()
                .firstName("Max")
                .lastName("Ivanov")
                .email("Max@gmail.com")
                .password("client1")
                .phone("16379872")
                .userRole(UserRole.DRIVER)
                .build();
        session.persist(driver);
        session.clear();

        driver.setUserRole(UserRole.ADMIN);
        session.update(driver);

        User actualUser = session.find(User.class, driver.getId());
        assertEquals(driver.getUserRole(), actualUser.getUserRole());
    }

    @Test
    void deleteUser() {
        User client = TestObjectsUtils.getClient("Max", "Ivanov1@gmail.com");
        session.persist(client);
        session.clear();

        session.remove(client);
        Review actualUser = session.find(Review.class, client.getId());

        assertNull(actualUser);
    }
}