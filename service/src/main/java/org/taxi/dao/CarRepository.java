package org.taxi.dao;

import jakarta.persistence.EntityManager;
import org.taxi.entity.Car;

public class CarRepository extends RepositoryBase<Long, Car> {

    public CarRepository(EntityManager entityManager) {
        super(Car.class, entityManager);
    }
}