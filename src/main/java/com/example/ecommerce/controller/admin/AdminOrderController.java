package com.example.ecommerce.controller.admin;

import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.OrderStatus;
import com.example.ecommerce.service.EmailService;
import com.example.ecommerce.service.OrderService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@Controller
public class AdminOrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private EmailService emailService;
    @GetMapping("/admin/order")
    public String showOrders(@RequestParam(name = "startDate", defaultValue = "2023-01-01") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                             @RequestParam(name = "endDate", defaultValue = "2023-12-31") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
                             @RequestParam(defaultValue = "all") String status,
                             @RequestParam(defaultValue = "1") int sort,
                             Model model) {
        if (startDate == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -1);
            startDate = calendar.getTime();
        }
        if (endDate == null) {
            endDate = new Date();
        }
        List<Order> ordersWithDate = orderService.getOrdersBetweenDates(startDate, endDate);
        List<Order> orders = new ArrayList<>();
        if(sort == 1) {
            ordersWithDate = sortById(ordersWithDate);
        } else {

            ordersWithDate = sortByDate(ordersWithDate);
        }
        if(status.compareTo("all") == 0) {
            model.addAttribute("status", "all");
            model.addAttribute("orders", ordersWithDate);
            model.addAttribute("totalOrder", ordersWithDate.size());
            return "Admin/order/index";
        }
        for(Order item : ordersWithDate) {
            if(item.getStatus() != null) {
                if(item.getStatus().toString().compareTo(status) == 0) {
                    orders.add(item);
                }
            }
        }

        model.addAttribute("orders", orders);
        model.addAttribute("totalOrder", orders.size());
        model.addAttribute("status", status);
        return "Admin/order/index";
    }

    @GetMapping("admin/order/detail/{id}")
    public String getDetailOrder(Model model, @PathVariable Long id) {
        var order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        return "Admin/order/detail-order";

    }
    @PostMapping("/admin/order/edit/shipped/{id}")
    public String changeStatusOrderToShipped(
            @PathVariable("id") Long id
    ) {
        System.out.println(id);
        var order = orderService.getOrderById(id);
        order.setStatus(OrderStatus.SHIPPED);
        orderService.updateOrder(order.getId(), order);
        return "redirect:/admin/order";
    }
    @PostMapping("/admin/order/edit/cancel/{id}")
    public String changeStatusOrderToCencel(
            @PathVariable("id") Long id,
            @RequestParam String cancellationReason
    ) throws MessagingException, IOException {
        var order = orderService.getOrderById(id);
        order.setCancellationReason(cancellationReason);
        order.setStatus(OrderStatus.CANCELED);
        orderService.updateOrder(order.getId(), order);
        emailService.sendCancelOrder(order.getCustomer().getEmail(), "Cancel Order", order);
        return "redirect:/admin/order";
    }
    @PostMapping("/admin/order/edit/confirm/{id}")
    public String changeStatusOrderFormPendingToDelivering(
            @PathVariable("id") Long id
    ) throws MessagingException, IOException {
        var order = orderService.getOrderById(id);
        order.setStatus(OrderStatus.DELIVERING);
        orderService.updateOrder(order.getId(), order);
        emailService.sendOrderDetail(order.getCustomer().getEmail(), "Order Tracking", order);

        return "redirect:/admin/order";
    }
    public List<Order> sortByDate(List<Order> orders) {
        Collections.sort(orders, Comparator.comparing(Order::getOrderDate, Comparator.reverseOrder()));
        return orders;
    }

    // Sort orders by ID
    public List<Order> sortById(List<Order> orders) {
        Collections.sort(orders, Comparator.comparing(Order::getId));
        return orders;
    }


}
