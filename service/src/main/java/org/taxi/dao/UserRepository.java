package org.taxi.dao;

import org.hibernate.SessionFactory;
import org.taxi.entity.User;

import static org.taxi.entity.QUser.user;

public class UserRepository extends RepositoryBase<Long, User> {


    public UserRepository(SessionFactory sessionFactory) {
        super(User.class, sessionFactory, user);
    }
}
