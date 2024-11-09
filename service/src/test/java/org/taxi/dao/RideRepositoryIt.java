package org.taxi.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.taxi.entity.Ride;
import org.taxi.util.AbstractHibernateTest;
import org.taxi.util.TestObjectsUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
}