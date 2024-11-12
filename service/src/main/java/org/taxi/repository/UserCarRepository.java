package org.taxi.repository;

import jakarta.persistence.EntityManager;
import org.taxi.entity.UserCar;

public class UserCarRepository extends RepositoryBase<Long, UserCar> {
    public UserCarRepository(EntityManager entityManager) {
        super(UserCar.class, entityManager);
    }
}
