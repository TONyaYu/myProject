package org.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.history.RevisionRepository;
import org.taxi.entity.User;

public interface UserRepository extends JpaRepository<User, Long>,
        QuerydslPredicateExecutor<User>,
        FilterUserRepository, RevisionRepository<User, Long, Integer> {
}


