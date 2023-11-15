package com.example.ecommerce.controller;

import com.example.ecommerce.model.Order;
import com.example.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Create a new order
    @PostMapping
    public String createOrder(@RequestBody Order order) {
        Order createdOrder = orderService.createOrder(order);
        return "";
    }

    // Get an order by its ID
    @GetMapping("/{id}")
    public String getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        return "";

    }

    // Update an existing order
    @PutMapping("/{id}")
    public String updateOrder(@PathVariable Long id, @RequestBody Order updatedOrder) {
        Order updated = orderService.updateOrder(id, updatedOrder);
        return "";

    }

    // Delete an order by its ID
    @DeleteMapping("/{id}")
    public String deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return "";

    }
}
