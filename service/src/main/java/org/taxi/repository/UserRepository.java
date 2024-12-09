package org.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.taxi.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
