package org.taxi.dao;

import org.hibernate.SessionFactory;
import org.taxi.entity.Car;
import org.taxi.entity.QCar;

public class CarRepository extends RepositoryBase<Long, Car> {

    public CarRepository(SessionFactory sessionFactory) {
        super(Car.class, sessionFactory, QCar.car);
    }
}