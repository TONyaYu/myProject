package org.taxi.dao;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.taxi.entity.Ride;
import org.taxi.entity.enums.RideStatus;
import org.taxi.util.AbstractHibernateTest;
import org.taxi.util.QueryDslPredicate;
import org.taxi.util.RideFilter;
import org.taxi.util.TestModels;
import org.taxi.util.TestObjectsUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.taxi.entity.QRide.ride;

class RideRepositoryIt extends AbstractHibernateTest {

    private RideRepository rideRepository;

    @BeforeEach
    void init() {
        rideRepository = new RideRepository(session);
    }

    @Test
    void insert() {
        Ride ride = TestObjectsUtils.getRide("Hotel", "Airport");

        Ride actualRide = rideRepository.save(ride);

        assertNotNull(actualRide.getId());
    }

    @Test
    void update() {
        Ride ride = TestObjectsUtils.getRide("Hotel", "Airport");
        ride.setStartLocation("Work");

        rideRepository.update(ride);
        session.clear();
        Optional<Ride> actualRide = rideRepository.findById(ride.getId());

        assertThat(actualRide)
                .isPresent()
                .contains(ride);
    }

    @Test
    void findById() {
        Ride ride = rideRepository.save(TestObjectsUtils.getRide("Mall", "Cinema"));
        session.clear();

        Optional<Ride> actualRide = rideRepository.findById(ride.getId());

        assertThat(actualRide)
                .isPresent()
                .contains(ride);
    }

    @Test
    void findAll() {
        Ride ride1 = rideRepository.save(TestObjectsUtils.getRide("Mall", "Cinema"));
        Ride ride2 = rideRepository.save(TestObjectsUtils.getRide("Hotel", "Airport"));
        Ride ride3 = rideRepository.save(TestObjectsUtils.getRide("Office", "Home"));

        List<Ride> actualRides = rideRepository.findAll();

        List<Long> rideIds = actualRides.stream()
                .map(Ride::getId)
                .toList();
        assertThat(rideIds).containsExactlyInAnyOrder(ride1.getId(), ride2.getId(), ride3.getId());
    }

    @ParameterizedTest
    @MethodSource("getExpectedSize")
    void checkFindAllRidesByFilter(RideFilter filter, Integer expected) {
        TestModels.importData(session);
        Predicate predicate = QueryDslPredicate.builder()
                .add(filter.getClient(), ride.client::eq)
                .add(filter.getDriver(), ride.driver::eq)
                .add(filter.getStartDate(), ride.startDate::eq)
                .add(filter.getStatus(), ride.status::eq)
                .add(filter.getCost(), ride.cost::eq)
                .add(filter.getStartLocation(), ride.startLocation::eq)
                .add(filter.getEndLocation(), ride.endLocation::eq)
                .buildAnd();

        List<Ride> actualRides = new JPAQuery<Ride>(session)
                .select(ride)
                .from(ride)
                .where(predicate)
                .fetch();

        assertThat(actualRides).hasSize(expected);
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
                                .status(RideStatus.COMPLETED)
                                .build(),
                        2
                ),
                // Фильтр по стоимости 15.00
                Arguments.of(
                        RideFilter.builder()
                                .cost(BigDecimal.valueOf(15.00))
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
                                .client(null)
                                .driver(null)
                                .startDate(LocalDateTime.of(2025, 1, 1, 0, 0))
                                .status(RideStatus.PLANNED)
                                .cost(BigDecimal.valueOf(100.00))
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

}