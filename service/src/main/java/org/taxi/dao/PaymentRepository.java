package org.taxi.dao;

import org.hibernate.SessionFactory;
import org.taxi.entity.Payment;

import static org.taxi.entity.QPayment.payment;

public class PaymentRepository extends RepositoryBase<Long, Payment> {

    public PaymentRepository(SessionFactory sessionFactory) {
        super(Payment.class, sessionFactory, payment);
    }
}
