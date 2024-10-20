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

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PaymentIT {

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
    void createPayment() {
        Ride ride = TestObjectsUtils.getRide("here", "there");
        Payment payment = Payment.builder()
                .paymentMethod(PayMethod.CASH)
                .amount(BigDecimal.valueOf(3.15))
                .date(LocalDateTime.now())
                .ride(ride)
                .build();
        session.persist(ride);
        session.persist(payment);

        session.clear();
        Assertions.assertNotNull(payment);
    }

    @Test
    void findPayment() {
        Ride ride = TestObjectsUtils.getRide("here", "there");
        Ride ride1 = TestObjectsUtils.getRide("home", "new address");
        Ride ride2 = TestObjectsUtils.getRide("vokzal", "home");
        Payment payment = Payment.builder()
                .paymentMethod(PayMethod.CASH)
                .amount(BigDecimal.valueOf(3.15))
                .date(LocalDateTime.now())
                .ride(ride)
                .build();
        Payment payment1 = Payment.builder()
                .paymentMethod(PayMethod.CARD)
                .amount(BigDecimal.valueOf(10.05))
                .date(LocalDateTime.now().plusMinutes(15))
                .ride(ride1)
                .build();
        Payment payment2 = Payment.builder()
                .paymentMethod(PayMethod.CASH)
                .amount(BigDecimal.valueOf(5))
                .date(LocalDateTime.now().minusDays(1))
                .ride(ride2)
                .build();
        session.persist(ride);
        session.persist(ride1);
        session.persist(ride2);
        session.persist(payment);
        session.persist(payment1);
        session.persist(payment2);
        session.clear();

        Payment actualPayment = session.find(Payment.class, payment2);
        assertEquals(payment2.getId(), actualPayment.getId());
    }

    @Test
    void updatePayment() {
        Ride ride = TestObjectsUtils.getRide("here", "there");
        Payment payment = Payment.builder()
                .paymentMethod(PayMethod.CASH)
                .amount(BigDecimal.valueOf(16.3))
                .date(LocalDateTime.now())
                .ride(ride)
                .build();
        session.persist(ride);
        session.persist(payment);

        session.clear();
        payment.setPaymentMethod(PayMethod.CARD);
        session.merge(payment);

        Payment actualPayment = session.find(Payment.class, payment);
        Assertions.assertEquals(payment.getPaymentMethod(), actualPayment.getPaymentMethod());
    }

    @Test
    void deletePayment() {
        Ride ride = TestObjectsUtils.getRide("here", "there");
        Payment payment = TestObjectsUtils.getPayment(ride);
        session.persist(ride);
        session.persist(payment);
        session.clear();

        session.remove(payment);
        Payment actualPayment = session.find(Payment.class, payment);
        assertNull(actualPayment);
    }
}