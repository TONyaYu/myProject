package org.taxi.entity;

import org.junit.jupiter.api.Test;
import org.taxi.util.AbstractHibernateTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CarIT extends AbstractHibernateTest {

    @Test
    void createCar() {
        Car car = Car.builder()
                .make("BMW")
                .model("x5")
                .licensePlate("125gf684")
                .isAvailable(true)
                .build();
        session.persist(car);
        session.clear();

        assertNotNull(car.getId());
    }

    @Test
    void updateCar() {
        Car car = Car.builder()
                .make("BMW")
                .model("x5")
                .licensePlate("125gf684")
                .isAvailable(true)
                .build();
        session.persist(car);
        session.clear();

        car.setAvailable(false);
        session.update(car);
        Car actualCar = session.get(Car.class, 1);

        assertEquals(car.isAvailable(), actualCar.isAvailable());
    }
}