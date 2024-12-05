package org.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.taxi.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long>,
        FilterPaymentRepository,
        QuerydslPredicateExecutor<Payment> {
}
