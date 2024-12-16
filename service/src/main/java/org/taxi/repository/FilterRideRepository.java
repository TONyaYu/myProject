package org.taxi.repository;

import org.taxi.entity.Ride;
import org.taxi.dto.filters.RideFilter;

import java.util.List;

public interface FilterRideRepository {

    List<Ride> findAllRideByFilter(RideFilter filter);
}
