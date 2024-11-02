package org.taxi.integration;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.taxi.entity.*;
import org.taxi.util.AbstractHibernateTest;
import org.taxi.util.TestModels;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
    private void filterPayments() {
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

            List<Payment> payments = new JPAQuery<Payment>((EntityManager) session)
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
    private void filterRides() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

            // Given
            User driver = session.createQuery("from User where email = 'toms@gmail.com'", User.class).uniqueResult();

            // When
            QRide qRide = QRide.ride;
            QReview qReview = QReview.review;
            QUser qUser = QUser.user;

            BooleanExpression predicate = qRide.startDate.eq(LocalDateTime.of(2024, 10, 30, 8, 0))
                    .and(qReview.rating.eq(5))
                    .and(qRide.status.eq(RideStatus.COMPLETED))
                    .and(qRide.cost.eq(BigDecimal.valueOf(15.00)))
                    .and(qRide.driver.eq(driver));

            List<Ride> rides = new JPAQuery<Ride>((EntityManager) session)
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
    private void filterByOrder() {
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

            List<Ride> rides = new JPAQuery<Ride>((EntityManager) session)
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
    private void filterDailyOrders() {
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

            List<Ride> rides = new JPAQuery<Ride>((EntityManager) session)
                    .select(qRide)
                    .join(qRide.driver, qUser)
                    .where(predicate)
                    .fetch();

            // Then
            assertThat(rides).hasSize(1);
            assertThat(rides.get(0).getCost()).isEqualTo(BigDecimal.valueOf(15.00));
        }
}
