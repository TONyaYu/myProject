package org.taxi.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.taxi.annotation.IT;
import org.taxi.entity.Payment;
import org.taxi.entity.Ride;
import org.taxi.entity.User;
import org.taxi.entity.enums.PayMethod;
import org.taxi.util.PaymentFilter;
import org.taxi.util.TestObjectsUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

@IT
@RequiredArgsConstructor
class PaymentRepositoryIT {

    private final PaymentRepository paymentRepository;
    private final EntityManager entityManager;

    @Test
    void savePayment() {
        Payment payment = getPayment("home", "aero");
        Payment expectedPayment = paymentRepository.save(payment);

        assertThat(expectedPayment.getId()).isNotNull();
    }

    @Test
    void deletePayment() {
        Payment payment = getPayment("home", "aero");
        paymentRepository.save(payment);

        paymentRepository.delete(payment);

        Optional<Payment> actualResult = paymentRepository.findById(payment.getId());
        assertFalse(actualResult.isPresent());
    }

    @Test
    void findPaymentById() {
        Payment payment = getPayment("home", "aero");
        Payment savedPayment = paymentRepository.save(payment);

        Payment expectedPayment = paymentRepository.findById(savedPayment.getId()).orElse(null);

        assertThat(expectedPayment).isNotNull();
        assertThat(expectedPayment.getId()).isEqualTo(savedPayment.getId());
    }

    @Test
    void updatePayment() {
        Payment payment = getPayment("home", "aero");
        Payment savedPayment = paymentRepository.save(payment);

        savedPayment.setAmount(new BigDecimal("20.00"));
        paymentRepository.update(savedPayment);

        Payment expectedPayment = paymentRepository.findById(savedPayment.getId()).orElse(null);
        assertThat(expectedPayment).isNotNull();
        assertThat(expectedPayment.getAmount()).isEqualTo(new BigDecimal("20.00"));
    }

    @Test
    void findAllPaymentsByFilter() {
        Payment payment1 = getPayment("home", "aero");
        Payment payment2 = getPayment("office", "mall");
        paymentRepository.save(payment1);
        paymentRepository.save(payment2);

        PaymentFilter filter = PaymentFilter.builder()
                .date(payment1.getDate())
                .amount(payment1.getAmount())
                .payMethod(payment1.getPaymentMethod())
                .build();

        List<Payment> payments = paymentRepository.findAllPaymentByFilter(filter);

        assertThat(payments).hasSize(1);
        assertThat(payments.get(0).getAmount()).isEqualTo(payment1.getAmount());
    }

    @ParameterizedTest
    @MethodSource("getExpectedSize")
    void checkFindAllPaymentsByFilter(PaymentFilter filter, Integer expected) {
        // Создаем и сохраняем платежи для теста
        Payment payment1 = getPayment("home", "aero");
        Payment payment2 = getPayment("office", "mall");
        Payment payment3 = getPayment("home", "office");
        Payment payment4 = getPayment("mall", "aero");
        payment1.setAmount(new BigDecimal("20.00"));
        payment4.setDate(LocalDateTime.of(2024, 10, 30, 8, 30));
        payment3.setPaymentMethod(PayMethod.CARD);
        paymentRepository.save(payment1);
        paymentRepository.save(payment2);
        paymentRepository.save(payment3);
        paymentRepository.save(payment4);

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
                        1
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

    private Payment getPayment(String start, String end) {
        String uniqueEmailClient = "Lev" + UUID.randomUUID() + "@gmail.com";
        String uniqueEmailDriver = "Max" + UUID.randomUUID() + "@gmail.com";

        User client = TestObjectsUtils.getClient("Lev", uniqueEmailClient);
        entityManager.persist(client);

        User driver = TestObjectsUtils.getDriver("Max", uniqueEmailDriver);
        entityManager.persist(driver);

        Ride ride = TestObjectsUtils.getRide(start, end);
        ride.setClient(client);
        ride.setDriver(driver);
        entityManager.persist(ride);

        Payment payment = TestObjectsUtils.getPayment(ride);
        entityManager.persist(payment);
        entityManager.flush();
        return payment;
    }
}