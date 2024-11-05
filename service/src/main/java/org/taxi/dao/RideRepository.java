package org.taxi.dao;

import org.hibernate.SessionFactory;
import org.taxi.entity.Ride;

import static org.taxi.entity.QRide.ride;

public class RideRepository extends RepositoryBase<Long, Ride> {

    public RideRepository(SessionFactory sessionFactory) {
        super(Ride.class, sessionFactory, ride);
    }
}
