package org.taxi.repository;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.taxi.entity.Car;

@Repository
public class CarRepository extends RepositoryBase<Long, Car> {

    public CarRepository(EntityManager entityManager) {
        super(Car.class, entityManager);
    }
}