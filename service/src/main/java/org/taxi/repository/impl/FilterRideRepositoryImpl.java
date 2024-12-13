package org.taxi.repository.impl;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.taxi.entity.Ride;
import org.taxi.filters.RideFilter;
import org.taxi.repository.FilterRideRepository;
import org.taxi.util.QueryDslPredicate;

import java.util.List;

import static org.taxi.entity.QRide.ride;
import static org.taxi.entity.QUser.user;

@RequiredArgsConstructor
public class FilterRideRepositoryImpl implements FilterRideRepository {

    private final EntityManager entityManager;

    @Override
    public List<Ride> findAllRideByFilter(RideFilter filter) {
        Predicate predicate = QueryDslPredicate.builder()
                .add(filter.getCost(), ride.cost::eq)
                .add(filter.getClientId(), user.id::eq)
                .add(filter.getDriverId(), user.id::eq)
                .add(filter.getStartDate(), ride.startDate::eq)
                .add(filter.getStartLocation(), ride.startLocation::eq)
                .add(filter.getEndLocation(), ride.endLocation::eq)
                .add(filter.getStatus(), ride.status::eq)
                .buildAnd();

        return new JPAQuery<Ride>(entityManager)
                .select(ride)
                .from(ride)
                .where(predicate)
                .fetch();
    }
}

