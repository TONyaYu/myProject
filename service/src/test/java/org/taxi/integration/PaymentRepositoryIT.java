package org.taxi.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.taxi.repository.PaymentRepository;
import org.taxi.entity.Payment;
import org.taxi.entity.enums.PayMethod;
import org.taxi.util.AbstractHibernateTest;
import org.taxi.util.PaymentFilter;
import org.taxi.util.TestModels;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentRepositoryIT extends AbstractHibernateTest {

    private PaymentRepository paymentRepository;

    @BeforeEach
    void init() {
        paymentRepository = new PaymentRepository(session);
    }

    @Test
    void findAllPaymentByFilter() {
        Payment expectedPayment = Payment.builder()
                .paymentMethod(PayMethod.CASH)
                .amount(new BigDecimal("15.00"))
                .date(LocalDateTime.of(2024, 10, 30, 8, 30))
                .build();
        session.save(expectedPayment);
        // Создаем фильтр для поиска платежей
        PaymentFilter filter = PaymentFilter.builder()
                .date(LocalDateTime.of(2024, 10, 30, 8, 30))
                .amount(new BigDecimal("15.00"))
                .payMethod(PayMethod.CASH)
                .build();

        List<Payment> payments = paymentRepository.findAllPaymentByFilter(filter);

        assertThat(payments).hasSize(1);
        assertThat(payments.get(0).getId()).isEqualTo(expectedPayment.getId());
    }

    @ParameterizedTest
    @MethodSource("getExpectedSize")
    void checkFindAllPaymentsByFilter(PaymentFilter filter, Integer expected) {
        TestModels.importData(session);
        List<Payment> payments = paymentRepository.findAllPaymentByFilter(filter);

        assertThat(payments).hasSize(expected);
    }


    static Stream<Arguments> getExpectedSize() {
        return Stream.of(
                // Фильтр по дате платежа
                Arguments.of(
                        PaymentFilter.builder()
                                .date(LocalDateTime.of(2024, 10, 30, 8, 30))
                                .build(),
                        1
                ),
                // Фильтр по сумме платежа
                Arguments.of(
                        PaymentFilter.builder()
                                .amount(new BigDecimal("20.00"))
                                .build(),
                        1
                ),
                // Фильтр по методу оплаты
                Arguments.of(
                        PaymentFilter.builder()
                                .payMethod(PayMethod.CARD)
                                .build(),
                        2
                ),
                // Фильтр по всем полям (нет результатов)
                Arguments.of(
                        PaymentFilter.builder()
                                .date(LocalDateTime.of(2025, 1, 1, 0, 0))
                                .amount(new BigDecimal("100.00"))
                                .payMethod(PayMethod.CASH)
                                .build(),
                        0
                ),
                // Фильтр по всем полям (все платежи)
                Arguments.of(
                        PaymentFilter.builder()
                                .build(),
                        4
                )
        );
    }
}