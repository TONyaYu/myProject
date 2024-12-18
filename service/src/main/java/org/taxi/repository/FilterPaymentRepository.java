package org.taxi.repository;

import org.taxi.entity.Payment;
import org.taxi.dto.filters.PaymentFilter;

import java.util.List;

public interface FilterPaymentRepository {

    List<Payment> findAllPaymentByFilter(PaymentFilter filter);
}
