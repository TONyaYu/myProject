package org.taxi.repository;

import org.taxi.entity.Ride;
import org.taxi.filters.RideFilter;

import java.util.List;

public interface FilterRideRepository {

    List<Ride> findAllRideByFilter(RideFilter filter);
}
