package org.taxi.repository.impl;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.taxi.entity.Payment;
import org.taxi.repository.FilterPaymentRepository;
import org.taxi.filters.PaymentFilter;
import org.taxi.util.QueryDslPredicate;

import java.util.ArrayList;
import java.util.List;

import static org.taxi.entity.QPayment.payment;
import static org.taxi.entity.QRide.ride;

@RequiredArgsConstructor
public class FilterPaymentRepositoryImpl implements FilterPaymentRepository {

    private final EntityManager entityManager;

    @Override
    public List<Payment> findAllPaymentByFilter(PaymentFilter filter) {
        Predicate predicate = QueryDslPredicate.builder()
                .add(filter.getDate(), payment.date::eq)
                .add(filter.getAmount(), payment.amount::eq)
                .add(filter.getRideId(), ride.id::eq)
                .add(filter.getPayMethod(), payment.paymentMethod::eq)
                .buildAnd();

        return new JPAQuery<Payment>(entityManager)
                .select(payment)
                .from(payment)
                .where(predicate)
                .fetch();
    }
}
