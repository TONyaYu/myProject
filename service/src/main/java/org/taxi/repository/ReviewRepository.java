package org.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.taxi.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
