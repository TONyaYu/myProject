package org.taxi.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import org.taxi.entity.Payment;
import org.taxi.util.PaymentFilter;
import org.taxi.util.QueryDslPredicate;

import java.util.List;

import static org.taxi.entity.QPayment.payment;
import static org.taxi.entity.QRide.ride;

public class PaymentRepository extends RepositoryBase<Long, Payment> {
    public PaymentRepository(EntityManager entityManager) {

        super(Payment.class, entityManager);
    }

    public List<Payment> findAllPaymentByFilter(PaymentFilter filter) {
        Predicate predicate = QueryDslPredicate.builder()
                .add(filter.getDate(), payment.date::eq)
                .add(filter.getAmount(), payment.amount::eq)
                .add(filter.getRideId(), ride.id::eq)
                .add(filter.getPayMethod(), payment.paymentMethod::eq)
                .buildAnd();

        return new JPAQuery<Payment>(getEntityManager())
                .select(payment)
                .from(payment)
                .where(predicate)
                .fetch();
    }
}
