package com.example.ecommerce.repo;

import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.OrderProduct;
import com.example.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepo extends JpaRepository<OrderProduct, Long> {
}
