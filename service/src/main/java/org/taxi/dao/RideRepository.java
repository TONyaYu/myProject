package org.taxi.dao;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import org.taxi.entity.Ride;
import org.taxi.util.QueryDslPredicate;
import org.taxi.util.RideFilter;

import java.util.List;

import static org.taxi.entity.QRide.ride;

public class RideRepository extends RepositoryBase<Long, Ride> {

    public RideRepository(EntityManager entityManager) {
        super(Ride.class, entityManager);
    }

    public List<Ride> findAllRideByFilter(RideFilter filter) {
        Predicate predicate = QueryDslPredicate.builder()
                .add(filter.getCost(), ride.cost::eq)
                .add(filter.getClient(), ride.client::eq)
                .add(filter.getDriver(), ride.driver::eq)
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
