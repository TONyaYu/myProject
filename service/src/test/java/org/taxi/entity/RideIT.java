package org.taxi.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.taxi.util.AbstractHibernateTest;
import org.taxi.util.TestObjectsUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNull;

class RideIT extends AbstractHibernateTest {

    @Test
    void createRide() {
        User driver = TestObjectsUtils.getDriver("Alex", "taxist@mail.ru");
        User client = TestObjectsUtils.getClient("Daria", "dasha@gmail.com");
        Ride ride = Ride.builder()
                .cost(BigDecimal.valueOf(3.25))
                .status(RideStatus.IN_PROGRESS)
                .startDate(LocalDateTime.now())
                .endLocation("where")
                .startLocation("here")
                .driver(driver)
                .user(client)
                .endDate(LocalDateTime.now().plusHours(1))
                .build();

        session.persist(driver);
        session.persist(client);
        session.persist(ride);
        session.clear();

        Assertions.assertNotNull(ride.getId());
    }

    @Test
    void updateRide() {
        User driver = TestObjectsUtils.getDriver("Alex", "taxist@mail.ru");
        User client = TestObjectsUtils.getClient("Daria", "dasha@gmail.com");
        Ride ride = Ride.builder()
                .cost(BigDecimal.valueOf(3.25))
                .status(RideStatus.IN_PROGRESS)
                .startDate(LocalDateTime.now())
                .endLocation("where")
                .startLocation("here")
                .driver(driver)
                .user(client)
                .endDate(LocalDateTime.now().plusHours(1))
                .build();
        session.persist(driver);
        session.persist(client);
        session.persist(ride);
        session.clear();

        ride.setStatus(RideStatus.COMPLETED);
        session.merge(ride);

        Ride actualRide = session.find(Ride.class, ride.getId());
        Assertions.assertEquals(ride.getStatus(), actualRide.getStatus());
    }

    @Test
    void deleteRide() {
        Ride ride = TestObjectsUtils.getRide("Tyt", "TaM");
        session.persist(ride);
        session.clear();

        session.remove(ride);
        Ride actualRide = session.find(Ride.class, ride.getId());

        assertNull(actualRide);
    }
}