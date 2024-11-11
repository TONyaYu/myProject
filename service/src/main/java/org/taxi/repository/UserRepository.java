package org.taxi.repository;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.taxi.entity.User;

@Repository
public class UserRepository extends RepositoryBase<Long, User> {
    public UserRepository(EntityManager entityManager) {
        super(User.class, entityManager);
    }
}
