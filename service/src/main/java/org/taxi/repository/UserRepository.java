package org.taxi.repository;

import jakarta.persistence.EntityManager;
import org.taxi.entity.User;

public class UserRepository extends RepositoryBase<Long, User> {
    public UserRepository(EntityManager entityManager) {
        super(User.class, entityManager);
    }
}
