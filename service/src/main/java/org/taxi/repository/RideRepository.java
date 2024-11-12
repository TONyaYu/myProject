package org.taxi.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.taxi.entity.Ride;
import org.taxi.util.QueryDslPredicate;
import org.taxi.util.RideFilter;

import java.util.List;

import static org.taxi.entity.QRide.ride;
import static org.taxi.entity.QUser.user;

@Repository
public class RideRepository extends RepositoryBase<Long, Ride> {

    public RideRepository(EntityManager entityManager) {
        super(Ride.class, entityManager);
    }

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

        return new JPAQuery<Ride>(getEntityManager())
                .select(ride)
                .from(ride)
                .where(predicate)
                .fetch();
    }
}
