package com.example.ecommerce.repo;

import com.example.ecommerce.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    // Custom query methods, if needed
}
