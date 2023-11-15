package com.example.ecommerce.service;

import com.example.ecommerce.model.OrderProduct;
import com.example.ecommerce.repo.OrderProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class OrderProductService {

    @Autowired
    private OrderProductRepo orderProductRepository;

    // Create a new order-product association
    public OrderProduct createOrderProduct(OrderProduct orderProduct) {
        return orderProductRepository.save(orderProduct);
    }

    // Get an order-product association by its ID
    public OrderProduct getOrderProductById(Long id) {
        Optional<OrderProduct> orderProductOptional = orderProductRepository.findById(id);
        return orderProductOptional.orElse(null);
    }

    // Update an existing order-product association
    public OrderProduct updateOrderProduct(Long id, OrderProduct updatedOrderProduct) {
        Optional<OrderProduct> orderProductOptional = orderProductRepository.findById(id);
        if (orderProductOptional.isPresent()) {
            OrderProduct existingOrderProduct = orderProductOptional.get();
            // Update fields of the existing order-product association with the new values
            existingOrderProduct.setQuantity(updatedOrderProduct.getQuantity());
            // Save the updated order-product association
            return orderProductRepository.save(existingOrderProduct);
        }
        return null; // Return null if the order-product association with the given ID does not exist
    }

    // Delete an order-product association by its ID
    public void deleteOrderProduct(Long id) {
        orderProductRepository.deleteById(id);
    }
}
