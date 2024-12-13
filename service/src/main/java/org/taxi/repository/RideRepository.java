package org.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.taxi.entity.Ride;

public interface RideRepository extends JpaRepository<Ride, Long>,
        FilterRideRepository,
        QuerydslPredicateExecutor<Ride> {
}
