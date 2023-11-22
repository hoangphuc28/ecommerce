package com.example.ecommerce.repo;

import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface OrderRepo extends JpaRepository<Order, Long> {
    List<Order> findByOrderDateBetween(Date startDate, Date endDate);
    List<Order> findByCustomerUser(User user);
    List<Order> findByCustomerEmail(String email);
    List<Order> findByCustomer_EmailOrCustomer_Phone(String email, String phone);

}
