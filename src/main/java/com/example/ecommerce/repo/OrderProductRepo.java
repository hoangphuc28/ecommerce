package com.example.ecommerce.repo;

import com.example.ecommerce.model.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepo extends JpaRepository<OrderProduct, Long> {
    // You can define custom query methods here if needed
}
