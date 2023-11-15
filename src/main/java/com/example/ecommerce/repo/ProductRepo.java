package com.example.ecommerce.repo;

import com.example.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductRepo extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
}
