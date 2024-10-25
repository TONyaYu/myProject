package org.taxi.integration;

import jakarta.persistence.criteria.JoinType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.taxi.entity.PayMethod;
import org.taxi.entity.Payment;
import org.taxi.entity.Ride;
import org.taxi.entity.RideStatus;
import org.taxi.util.HibernateUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class CriteriaIT {
    private static SessionFactory sessionFactory;

    @BeforeAll
    static void init() {
        sessionFactory = HibernateUtil.buildSessionFactory();
    }

    @Test
    public List<Payment> filterPayments(Session session) {
//клиент
//получения информации об оплатах по месту отправления, месту завершения поездки, водителю и способу оплаты
//        SELECT
//        p.id AS payment_id,
//                r.start_location,
//                r.end_location,
//                u.first_name AS driver_first_name,
//        u.last_name AS driver_last_name,
//                p.payment_method,
//                p.amount,
//                p.date AS payment_date
//        FROM
//        payment p
//        JOIN
//        ride r ON p.ride_id = r.id
//        JOIN
//        users u ON r.driver_id = u.id
//        WHERE
//        r.start_location = 'home'
//        AND r.end_location = 'work'
//        AND u.id = 1
//        AND p.payment_method = 'CARD'
//        ORDER BY
//        p.date

        var cb = session.getCriteriaBuilder();

        var criteria = cb.createQuery(Payment.class);
        var payment = criteria.from(Payment.class);
        var ride = payment.join("ride");
        var user = ride.join("driver");

        criteria.select(payment).where(
                cb.equal(ride.get("startLocation"), "home"),
                cb.equal(ride.get("endLocation"), "work"),
                cb.equal(user.get("id"), 1),
                cb.equal(payment.get("paymentMethod"), PayMethod.CASH)
        ).orderBy(cb.asc(payment.get("date")));
        return session.createQuery(criteria)
                .list();
    }

    @Test
    public List<Ride> filterRides(Session session) {
        //возвращает список поездок по дате, проставленному рейтингу, статусу, стоимости, водителю
//        "SELECT r.id, r.start_location, r.end_location, r.start_date, r.end_date, r.status, r.cost, u.first_name, u.last_name, rev.rating
//        FROM ride r
//        JOIN users u ON r.driver_id = u.id
//        JOIN review rev ON r.id = rev.ride_id
//        WHERE r.start_date BETWEEN '2023-01-01 00:00:00' AND '2023-12-31 23:59:59'
//        AND rev.rating = 5
//        AND r.status = 'COMPLETED'
//        AND r.cost BETWEEN 100 AND 500
//        AND u.id = 1
//        ORDER BY r.start_date"
        var cb = session.getCriteriaBuilder();

        var criteria = cb.createQuery(Ride.class);
        var ride = criteria.from(Ride.class);
        var review = ride.join("review", JoinType.LEFT);
        var user = ride.join("driver");

        criteria.select(ride).where(
                cb.between(ride.get("startDate"), LocalDateTime.now(), LocalDateTime.now().plusHours(1)),
        cb.equal(ride.get("rating"), 5),
        cb.equal(ride.get("status"), RideStatus.COMPLETED),
        cb.equal(ride.get("cost"), 5.9),
        cb.equal(ride.get("id"), 1)
        ).orderBy(cb.asc(ride.get("startDate")));

        return session.createQuery(criteria)
                .list();
    }

    @Test
    public List<Ride> filterByOrder(Session session) {
    //водитель
    //показывает информацию о заказе: стоимость, адрес отправления/посадки, адрес назначения, клиент
        var cb = session.getCriteriaBuilder();

        var criteria = cb.createQuery(Ride.class);
        var ride = criteria.from(Ride.class);
        var user = ride.join("user");

        criteria.select(ride).where(
                cb.equal(ride.get("cost"), 3.17),
                cb.equal(ride.get("startLocation"), "work"),
                cb.equal(ride.get("endLocation"), "home"),
                cb.equal(ride.get("firstName"), "Max"),
                cb.equal(ride.get("lastName"), "Ivanov"),
                cb.equal(ride.get("id"), 2)
        );

        return session.createQuery(criteria).list();
    }

    @Test
    public List<Ride> filterDailyOrders(Session session) {
        //админ
        //ппосмотреть все заказы за день, по водителю, стоимости. Сформировать суммарную выручку за день
        var cb = session.getCriteriaBuilder();

        var criteria = cb.createQuery(Ride.class);
        var ride = criteria.from(Ride.class);
        var user = ride.join("driver");

        criteria.multiselect(
                cb.equal(ride.get("id"), 12),
                cb.equal(ride.get("startLocation"), "work"),
                cb.equal(ride.get("endLocation"), "home"),
                cb.equal(ride.get("cost"), 3.17),
                cb.equal(user.get("firstName"), "Max"),
                cb.equal(user.get("firstName"), "Ivanov")
                );

        criteria.select(ride).where(

                cb.equal(cb.function("TRUNC", LocalDate.class, ride.get("startDate")), LocalDate.of(2024, 10, 15)),
                cb.equal(user.get("id"), 1),
                cb.between(ride.get("cost"), 1, 10)
                ).orderBy(cb.asc(ride.get("startDate")));


        return session.createQuery(criteria).list();
    }


}
