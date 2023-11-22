package com.example.ecommerce.service;

import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repo.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepo orderRepository;
    public List<Order> getOrdersByCustomerEmail(String email) {
        return orderRepository.findByCustomerEmail(email);
    }
    public List<Order> getOrdersByEmailOrPhone(String str) {
        return orderRepository.findByCustomer_EmailOrCustomer_Phone(str, str);
    }
    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByCustomerUser(user);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    // Create a new order
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    // Get an order by its ID
    public Order getOrderById(Long id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        return orderOptional.orElse(null);
    }
    public List<Order> getOrdersBetweenDates(Date startDate, Date endDate) {
        System.out.println(startDate);
        return orderRepository.findByOrderDateBetween(startDate, endDate);
    }

    // Update an existing order
    public Order updateOrder(Long id, Order updatedOrder) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isPresent()) {
            Order existingOrder = orderOptional.get();
            // Update fields of the existing order with the new values
            existingOrder.setOrderDate(updatedOrder.getOrderDate());
            existingOrder.setTotalAmount(updatedOrder.getTotalAmount());
            // Save the updated order
            return orderRepository.save(existingOrder);
        }
        return null; // Return null if the order with the given ID does not exist
    }

    // Delete an order by its ID
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
