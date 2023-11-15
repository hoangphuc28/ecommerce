package com.example.ecommerce.repo;

import com.example.ecommerce.model.Category;
import com.example.ecommerce.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category, Long> {
}

