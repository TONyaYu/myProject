package org.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.history.RevisionRepository;
import org.taxi.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends
        JpaRepository<User, Long>,
        QuerydslPredicateExecutor<User>,
        FilterUserRepository,
        RevisionRepository<User, Long, Integer> {

    @Query(value = "SELECT u.* FROM users u WHERE u.email = :email",
            nativeQuery = true)
    List<User> findAllByEmail(String email);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}


