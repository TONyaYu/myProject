package org.taxi.integration;

import jakarta.persistence.criteria.JoinType;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeAll;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class CriteriaIT extends AbstractHibernateTest {

    @BeforeAll
    static void initBD() {
        TestModels.importData(sessionFactory);
    }

    @Test
    void filterPayments() {
        filterPayments(session);
    }

    private void filterPayments(Session session) {
//клиент
//получения информации об оплатах по месту отправления, месту завершения поездки, водителю и способу оплаты
        var cb = session.getCriteriaBuilder();

        var criteria = cb.createQuery(Payment.class);
        var payment = criteria.from(Payment.class);
        var ride = payment.join(Payment_.RIDE);
        var user = ride.join(Ride_.DRIVER);

        criteria.select(payment).where(
                cb.equal(ride.get(Ride_.START_LOCATION), "home"),
                cb.equal(ride.get(Ride_.END_LOCATION), "work"),
                cb.equal(user.get(User_.ID), 1),
                cb.equal(payment.get(Payment_.PAYMENT_METHOD), PayMethod.CASH)
        ).orderBy(cb.asc(payment.get(Payment_.DATE)));
        session.createQuery(criteria)
                .list();
    }

    @Test
    void filterRides() {
        List<Ride> rides = filterRides(session);
    }

    private List<Ride> filterRides(Session session) {
        //возвращает список поездок по дате, проставленному рейтингу, статусу, стоимости, водителю
        var cb = session.getCriteriaBuilder();

        var criteria = cb.createQuery(Ride.class);
        var ride = criteria.from(Ride.class);
        var review = ride.join(Ride_.reviews, JoinType.LEFT);
        var user = ride.join(Ride_.DRIVER);

        criteria.select(ride).where(
                cb.between(ride.get(Ride_.START_DATE), LocalDateTime.now(), LocalDateTime.now().plusHours(1)),
                cb.equal(review.get(Review_.RATING), 5),
                cb.equal(ride.get(Ride_.STATUS), RideStatus.COMPLETED),
                cb.equal(ride.get(Ride_.COST), 15),
                cb.equal(ride.get(Ride_.ID), 1)
        ).orderBy(cb.asc(ride.get(Ride_.START_DATE)));

        return session.createQuery(criteria)
                .list();
    }

    @Test
    void filterByOrder() {
        List<Ride> rides = filterRides(session);
    }

    private List<Ride> filterByOrder(Session session) {
        //водитель
        //показывает информацию о заказе: стоимость, адрес отправления/посадки, адрес назначения, клиент
        var cb = session.getCriteriaBuilder();

        var criteria = cb.createQuery(Ride.class);
        var ride = criteria.from(Ride.class);
        var user = ride.join(Ride_.USER);

        criteria.select(ride).where(
                cb.equal(ride.get(Ride_.COST), 20),
                cb.equal(ride.get(Ride_.START_LOCATION), "work"),
                cb.equal(ride.get(Ride_.END_LOCATION), "home"),
                cb.equal(user.get(User_.FIRST_NAME), "Max"),
                cb.equal(user.get(User_.LAST_NAME), "Ivanov"),
                cb.equal(user.get(User_.ID), 2)
        );

        return session.createQuery(criteria).list();
    }

    @Test
    void filterDailyOrders() {
        List<Ride> rides = filterRides(session);
    }

    public List<Ride> filterDailyOrders(Session session) {
        //админ
        //посмотреть все заказы за день, по водителю, стоимости.
        var cb = session.getCriteriaBuilder();

        var criteria = cb.createQuery(Ride.class);
        var ride = criteria.from(Ride.class);
        var user = ride.join(Ride_.DRIVER);

        criteria.multiselect(
                cb.equal(ride.get(Ride_.ID), 12),
                cb.equal(ride.get(Ride_.START_LOCATION), "work"),
                cb.equal(ride.get(Ride_.END_LOCATION), "home"),
                cb.equal(ride.get(Ride_.COST), 3.17),
                cb.equal(user.get(User_.FIRST_NAME), "Max"),
                cb.equal(user.get(User_.LAST_NAME), "Ivanov")
        );

        criteria.select(ride).where(

                cb.equal(cb.function("TRUNC", LocalDate.class, ride.get(Ride_.START_DATE)), LocalDate.of(2024, 10, 15)),
                cb.equal(user.get(User_.ID), 1),
                cb.between(ride.get(Ride_.COST), 1, 10)
        ).orderBy(cb.asc(ride.get(Ride_.START_DATE)));


        return session.createQuery(criteria).list();
    }


}
