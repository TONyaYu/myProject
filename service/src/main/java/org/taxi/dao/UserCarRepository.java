package org.taxi.dao;

import org.hibernate.SessionFactory;
import org.taxi.entity.UserCar;

import static org.taxi.entity.QUserCar.userCar;

public class UserCarRepository extends RepositoryBase<Long, UserCar> {
    public UserCarRepository(SessionFactory sessionFactory) {
        super(UserCar.class, sessionFactory, userCar);
    }
}
