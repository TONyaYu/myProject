package org.taxi.integration;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.graph.GraphSemantic;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.taxi.entity.PayMethod;
import org.taxi.entity.Payment;
import org.taxi.entity.QPayment;
import org.taxi.entity.QReview;
import org.taxi.entity.QRide;
import org.taxi.entity.QUser;
import org.taxi.entity.Ride;
import org.taxi.entity.RideStatus;
import org.taxi.entity.User;
import org.taxi.util.AbstractHibernateTest;
import org.taxi.util.TestModels;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class QueryDslIT extends AbstractHibernateTest {

    @BeforeAll
    static void initBD() {
        Configuration configuration = new Configuration().configure();
        TestModels.importData(sessionFactory);
    }

    //клиент
    //получения информации об оплатах по месту отправления, месту завершения поездки, водителю и способу оплаты
    @Test
    void filterPayments() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();
        // Given
        User driver = session.createQuery("from User where email = 'toms@gmail.com'", User.class).uniqueResult();

        // When
        QPayment qPayment = QPayment.payment;
        QRide qRide = QRide.ride;
        QUser qUser = QUser.user;

        BooleanExpression predicate = qPayment.ride.startLocation.eq("Home")
                .and(qPayment.ride.endLocation.eq("Work"))
                .and(qPayment.ride.driver.eq(driver))
                .and(qPayment.paymentMethod.eq(PayMethod.CASH));

        List<Payment> payments = new JPAQuery<Payment>(session)
                .select(qPayment)
                .join(qPayment.ride, qRide)
                .join(qRide.driver, qUser)
                .where(predicate)
                .fetch();

        // Then
        assertThat(payments).hasSize(1);
        assertThat(payments.get(0).getAmount()).isEqualTo(BigDecimal.valueOf(15.00));
    }


    //возвращает список поездок по дате, проставленному рейтингу, статусу, стоимости, водителю
    @Test
    void filterRides() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        var userGraph = session.createEntityGraph(Ride.class);
        userGraph.addAttributeNodes("review", "user");

        Map<String, Object> properties = Map.of(GraphSemantic.LOAD.getJakartaHintName(), userGraph);

        // Given
        User driver = session.createQuery("select u from User u where email = 'toms@gmail.com'", User.class)
                .setHint(GraphSemantic.LOAD.getJakartaHintName(), userGraph)
                .uniqueResult();

        // When
        QRide qRide = QRide.ride;
        QReview qReview = QReview.review;
        QUser qUser = QUser.user;

        BooleanExpression predicate = qRide.startDate.eq(LocalDateTime.of(2024, 10, 30, 8, 0))
                .and(qReview.rating.eq(5))
                .and(qRide.status.eq(RideStatus.COMPLETED))
                .and(qRide.cost.eq(BigDecimal.valueOf(15.00)))
                .and(qRide.driver.eq(driver));

        List<Ride> rides = new JPAQuery<Ride>(session)
                .select(qRide)
                .join(qRide.reviews, qReview)
                .join(qRide.driver, qUser)
                .where(predicate)
                .fetch();

        // Then
        assertThat(rides).hasSize(1);
        assertThat(rides.get(0).getCost()).isEqualTo(BigDecimal.valueOf(15.00));
    }


    //водитель
    //показывает информацию о заказе: стоимость, адрес отправления/посадки, адрес назначения, клиент
    @Test
    void filterByOrder() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        // Given
        User user = session.createQuery("from User where email = 'alice@gmail.com'", User.class).uniqueResult();

        // When
        QRide qRide = QRide.ride;
        QUser qUser = QUser.user;

        BooleanExpression predicate = qRide.cost.eq(BigDecimal.valueOf(15.00))
                .and(qRide.startLocation.eq("Home"))
                .and(qRide.endLocation.eq("Work"))
                .and(qRide.user.eq(user));

        List<Ride> rides = new JPAQuery<Ride>(session)
                .select(qRide)
                .join(qRide.user, qUser)
                .where(predicate)
                .fetch();

        // Then
        assertThat(rides).hasSize(1);
        assertThat(rides.get(0).getCost()).isEqualTo(BigDecimal.valueOf(15.00));
    }


    //админ
    //посмотреть все заказы за день, по водителю, стоимости.
    @Test
    void filterDailyOrders() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        // Given
        User driver = session.createQuery("from User where email = 'toms@gmail.com'", User.class).uniqueResult();

        // When
        QRide qRide = QRide.ride;
        QUser qUser = QUser.user;

        BooleanExpression predicate = qRide.startDate.between(LocalDateTime.of(2024, 10, 30, 0, 0), LocalDateTime.of(2024, 10, 30, 23, 59))
                .and(qRide.driver.eq(driver))
                .and(qRide.cost.eq(BigDecimal.valueOf(15.00)));

        List<Ride> rides = new JPAQuery<Ride>(session)
                .select(qRide)
                .join(qRide.driver, qUser)
                .where(predicate)
                .fetch();

        // Then
        assertThat(rides).hasSize(1);
        assertThat(rides.get(0).getCost()).isEqualTo(BigDecimal.valueOf(15.00));
    }
}
