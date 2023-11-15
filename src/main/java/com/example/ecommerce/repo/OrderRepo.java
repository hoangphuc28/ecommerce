package com.example.ecommerce.repo;

import com.example.ecommerce.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface OrderRepo extends JpaRepository<Order, Long> {
    List<Order> findByOrderDateBetween(Date startDate, Date endDate);
}
