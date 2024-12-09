package org.taxi.repository;

import org.springframework.data.repository.Repository;
import org.taxi.entity.Car;

public interface CarRepository extends Repository<Car, Long> {

}