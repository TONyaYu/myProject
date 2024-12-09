package org.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.taxi.entity.User;
import org.taxi.util.QueryDslPredicate;

public interface UserRepository extends JpaRepository<User, Long>,
        QuerydslPredicateExecutor<User> {
}
