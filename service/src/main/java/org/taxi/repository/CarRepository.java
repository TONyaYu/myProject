package org.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.taxi.entity.Car;

public interface CarRepository extends JpaRepository<Car, Long> {

}