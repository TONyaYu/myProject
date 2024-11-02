package org.taxi.integration;

import jakarta.persistence.criteria.JoinType;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.taxi.entity.PayMethod;
import org.taxi.entity.Payment;
import org.taxi.entity.Payment_;
import org.taxi.entity.Review_;
import org.taxi.entity.Ride;
import org.taxi.entity.RideStatus;
import org.taxi.entity.Ride_;
import org.taxi.entity.User_;
import org.taxi.util.AbstractHibernateTest;
import org.taxi.util.TestModels;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CriteriaIT extends AbstractHibernateTest {

    @BeforeAll
    static void initBD() {
        TestModels.importData(sessionFactory);
    }

    @BeforeEach
    void openSession() {
        session = sessionFactory.openSession();
        session.beginTransaction();
    }

    @Test
    private void filterPayments() {
        //клиент
        //получения информации об оплатах по месту отправления, месту завершения поездки, водителю и способу оплаты
        var cb = session.getCriteriaBuilder();

        var criteria = cb.createQuery(Payment.class);
        var payment = criteria.from(Payment.class);
        var ride = payment.join(Payment_.RIDE);
        var user = ride.join(Ride_.DRIVER);

        criteria.select(payment).where(
                cb.equal(ride.get(Ride_.START_LOCATION), "Home"),
                cb.equal(ride.get(Ride_.END_LOCATION), "Work"),
                cb.equal(user.get(User_.ID), 1),
                cb.equal(payment.get(Payment_.PAYMENT_METHOD), PayMethod.CASH)
        ).orderBy(cb.asc(payment.get(Payment_.DATE)));
        List<Payment> payments = session.createQuery(criteria)
                .list();

        // Then
        assertThat(payments).hasSize(1);
        assertThat(payments.get(0).getAmount()).isEqualTo(BigDecimal.valueOf(15.00));
    }

    @Test
    private void filterRides() {
        //возвращает список поездок по дате, проставленному рейтингу, статусу, стоимости, водителю
        var cb = session.getCriteriaBuilder();

        var criteria = cb.createQuery(Ride.class);
        var ride = criteria.from(Ride.class);
        var review = ride.join(Ride_.reviews, JoinType.LEFT);
        var user = ride.join(Ride_.DRIVER);

        criteria.select(ride).where(
                cb.equal(ride.get(Ride_.START_DATE), LocalDateTime.of(2024, 10, 30, 8, 0)),
                cb.equal(review.get(Review_.RATING), 5),
                cb.equal(ride.get(Ride_.STATUS), RideStatus.COMPLETED),
                cb.equal(ride.get(Ride_.COST), 15)
        ).orderBy(cb.asc(ride.get(Ride_.START_DATE)));

        List<Ride> rides = session.createQuery(criteria)
                .list();

        assertThat(rides).hasSize(1);
        assertThat(rides.get(0).getCost()).isEqualTo(BigDecimal.valueOf(15.00));
    }

    @Test
    private void filterByOrder() {
        //водитель
        //показывает информацию о заказе: стоимость, адрес отправления/посадки, адрес назначения, клиент
        var cb = session.getCriteriaBuilder();

        var criteria = cb.createQuery(Ride.class);
        var ride = criteria.from(Ride.class);
        var user = ride.join(Ride_.USER);

        criteria.select(ride).where(
                cb.equal(ride.get(Ride_.COST), 20),
                cb.equal(ride.get(Ride_.START_LOCATION), "Airport"),
                cb.equal(ride.get(Ride_.END_LOCATION), "Hotel"),
                cb.equal(user.get(User_.FIRST_NAME), "Bob"),
                cb.equal(user.get(User_.LAST_NAME), "Smith"),
                cb.equal(user.get(User_.ID), 2)
        );

        List<Ride> rides = session.createQuery(criteria).list();

        assertThat(rides).hasSize(1);
        assertThat(rides.get(0).getCost()).isEqualTo(BigDecimal.valueOf(20.00));
    }

    @Test
    private void filterDailyOrders() {
        //админ
        //посмотреть все заказы за день, по водителю, стоимости.
        var cb = session.getCriteriaBuilder();

        var criteria = cb.createQuery(Ride.class);
        var ride = criteria.from(Ride.class);
        var user = ride.join(Ride_.DRIVER);

        criteria.select(ride).where(
                cb.equal(cb.function("TRUNC", LocalDate.class, ride.get(Ride_.START_DATE)), LocalDate.of(2024, 10, 30)),
                cb.equal(user.get(User_.ID), 1),
                cb.between(ride.get(Ride_.COST), 1, 100)
        ).orderBy(cb.asc(ride.get(Ride_.START_DATE)));

        List<Ride> rides = session.createQuery(criteria).list();

        assertThat(rides).hasSize(1);
        assertThat(rides.get(0).getCost()).isEqualTo(BigDecimal.valueOf(15.00));
    }
}