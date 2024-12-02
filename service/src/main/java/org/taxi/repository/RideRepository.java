package org.taxi.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.taxi.entity.Ride;
import org.taxi.util.QueryDslPredicate;
import org.taxi.util.RideFilter;

import java.util.List;

import static org.taxi.entity.QRide.ride;
import static org.taxi.entity.QUser.user;

public interface RideRepository extends JpaRepository<Ride, Long> {

    public default List<Ride> findAllRidesByFilter(RideFilter filter) {
        Predicate predicate = QueryDslPredicate.builder()
                .add(filter.getCost(), ride.cost::eq)
                .add(filter.getClientId(), user.id::eq)
                .add(filter.getDriverId(), user.id::eq)
                .add(filter.getStartDate(), ride.startDate::eq)
                .add(filter.getStartLocation(), ride.startLocation::eq)
                .add(filter.getEndLocation(), ride.endLocation::eq)
                .add(filter.getStatus(), ride.status::eq)
                .buildAnd();

        return new JPAQuery<Ride>()
                .select(ride)
                .from(ride)
                .where(predicate)
                .fetch();
    }
}
