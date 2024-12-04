package org.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.taxi.entity.UserCar;

public interface UserCarRepository extends JpaRepository<UserCar, Long> {
}
