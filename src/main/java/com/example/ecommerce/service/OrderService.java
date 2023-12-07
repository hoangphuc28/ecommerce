package com.example.ecommerce.service;

import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.OrderStatus;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repo.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.IsoFields;
import java.util.*;
import java.util.stream.Collectors;

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


    public  Map<String, BigDecimal> calculateAmountByMonth() {
        var orders = orderRepository.findAll();
        Map<String, BigDecimal> result = new HashMap<>();

        Map<String, List<Order>> ordersByMonth = orders.stream()
                .collect(Collectors.groupingBy(order -> getMonthYear(order.getOrderDate())));

        for (Map.Entry<String, List<Order>> entry : ordersByMonth.entrySet()) {
            BigDecimal totalAmount = entry.getValue().stream().filter(o -> o.getStatus() == OrderStatus.SHIPPED)
                    .map(Order::getTotalAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            result.put(entry.getKey(), totalAmount);
        }

        return result;
    }

    private static String getMonthYear(Date date) {

        return String.format("%d", date.getMonth()+1);
    }
    public Map<String, BigDecimal> calculateAmountByQuarter() {
        List<Order> orders = orderRepository.findAll(); // Replace this with your actual query

        Map<String, BigDecimal> quarterlyAmounts = new HashMap<>();

        for (Order order : orders) {
            if(order.getStatus() == OrderStatus.SHIPPED) {
                String quarter = getQuarter(order.getOrderDate());
                BigDecimal currentAmount = quarterlyAmounts.getOrDefault(quarter, BigDecimal.ZERO);
                BigDecimal newAmount = currentAmount.add(order.getTotalAmount());
                quarterlyAmounts.put(quarter, newAmount);
            }

        }

        return quarterlyAmounts;
    }

    private String getQuarter(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH);

        if (month >= 0 && month <= 2) {
            return "Q1";
        } else if (month >= 3 && month <= 5) {
            return "Q2";
        } else if (month >= 6 && month <= 8) {
            return "Q3";
        } else {
            return "Q4";
        }
    }





}
