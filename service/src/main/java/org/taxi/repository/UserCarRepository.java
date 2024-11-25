package org.taxi.repository;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.taxi.entity.UserCar;

@Repository
public class UserCarRepository extends RepositoryBase<Long, UserCar> {
    public UserCarRepository(EntityManager entityManager) {
        super(UserCar.class, entityManager);
    }
}
