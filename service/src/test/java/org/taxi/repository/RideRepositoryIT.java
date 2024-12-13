package org.taxi.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.taxi.annotation.IT;
import org.taxi.entity.Ride;
import org.taxi.entity.User;
import org.taxi.entity.enums.RideStatus;
import org.taxi.filters.RideFilter;
import org.taxi.util.TestObjectsUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IT
@RequiredArgsConstructor
class RideRepositoryIT {

    @Autowired
    private final RideRepository rideRepository;
    @Autowired
    private final EntityManager entityManager;

    @Test
    void saveRide() {
        Ride ride = getRide("Hotel", "Airport");

        Ride expectedRide = rideRepository.save(ride);

        assertThat(expectedRide.getId()).isNotNull();
    }

    @Test
    void deleteRide() {
        var ride = getRide("Hotel", "Airport");
        rideRepository.save(ride);
        Optional<Ride> actualResult = rideRepository.findById(ride.getId());
        assertTrue(actualResult.isPresent());

        actualResult.ifPresent(rideRepository::delete);
        entityManager.flush();

        assertTrue(rideRepository.findById(ride.getId()).isEmpty());
    }

    @Test
    void findRideById() {
        Ride ride = getRide("Hotel", "Airport");
        Ride savedRide = rideRepository.save(ride);

        Ride expectedRide = rideRepository.findById(savedRide.getId()).orElse(null);
        entityManager.flush();

        assertThat(expectedRide).isNotNull();
        assertThat(expectedRide.getId()).isEqualTo(savedRide.getId());
    }

    @Test
    void updateRide() {
        Ride ride = getRide("Hotel", "Airport");
        Ride savedRide = rideRepository.save(ride);

        savedRide.setCost(new BigDecimal("20.00"));
        rideRepository.save(savedRide);

        Ride expectedRide = rideRepository.findById(savedRide.getId()).orElse(null);
        assertThat(expectedRide).isNotNull();
        assertThat(expectedRide.getCost()).isEqualTo(new BigDecimal("20.00"));
    }

    @Test
    void findAllRideByFilter() {
        Ride ride1 = getRide("Mall", "Cinema");
        Ride ride2 = getRide("Hotel", "Airport");
        rideRepository.save(ride1);
        ride2.setStatus(RideStatus.PLANNED);
        rideRepository.save(ride2);


        List<Ride> rides = rideRepository.findAllRideByFilter(RideFilter.builder()
                .startDate(ride1.getStartDate())
                .cost(ride1.getCost())
                .status(ride1.getStatus())
                .build());

        assertThat(rides).hasSize(1);
        assertThat(rides.get(0).getId()).isEqualTo(ride1.getId());
    }

    @ParameterizedTest
    @MethodSource("getExpectedSize")
    void checkFindAllRidesByFilter(RideFilter filter, Integer expected) {
        // Создаем и сохраняем поездки для теста
        Ride ride1 = getRide("Mall", "Cinema");
        Ride ride2 = getRide("Hotel", "Airport");
        Ride ride3 = getRide("Office", "Home");
        Ride ride4 = getRide("Home", "Work");
        ride1.setCost(new BigDecimal("20.00"));
        ride4.setStartDate(LocalDateTime.of(2024, 10, 30, 8, 0));
        ride3.setStatus(RideStatus.PLANNED);
        rideRepository.save(ride1);
        rideRepository.save(ride2);
        rideRepository.save(ride3);
        rideRepository.save(ride4);

        List<Ride> rides = rideRepository.findAllRideByFilter(filter);

        assertThat(rides).hasSize(expected);
    }

    static Stream<Arguments> getExpectedSize() {
        return Stream.of(
                // Фильтр по дате начала поездки
                Arguments.of(
                        RideFilter.builder()
                                .startDate(LocalDateTime.of(2024, 10, 30, 8, 0))
                                .build(),
                        1
                ),
                // Фильтр по статусу COMPLETED
                Arguments.of(
                        RideFilter.builder()
                                .status(RideStatus.PLANNED)
                                .build(),
                        1
                ),
                // Фильтр по стоимости 20.00
                Arguments.of(
                        RideFilter.builder()
                                .cost(new BigDecimal("20.00"))
                                .build(),
                        1
                ),
                // Фильтр по начальной локации "Home"
                Arguments.of(
                        RideFilter.builder()
                                .startLocation("Home")
                                .build(),
                        1
                ),
                // Фильтр по конечной локации "Work"
                Arguments.of(
                        RideFilter.builder()
                                .endLocation("Work")
                                .build(),
                        1
                ),
                // Фильтр по всем полям (нет результатов)
                Arguments.of(
                        RideFilter.builder()
                                .startDate(LocalDateTime.of(2025, 1, 1, 0, 0))
                                .status(RideStatus.PLANNED)
                                .cost(new BigDecimal("100.00"))
                                .startLocation("Nonexistent")
                                .endLocation("Nonexistent")
                                .build(),
                        0
                ),
                // Фильтр по всем полям (все поездки)
                Arguments.of(
                        RideFilter.builder()
                                .build(),
                        4
                )
        );
    }

    private Ride getRide(String start, String end) {
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
        entityManager.flush();
        return ride;
    }
}