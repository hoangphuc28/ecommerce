package com.example.ecommerce.controller.admin;

import com.example.ecommerce.model.Customer;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.OrderProduct;
import com.example.ecommerce.model.User;
import com.example.ecommerce.service.CustomUserDetailsService;
import com.example.ecommerce.service.CustomerService;
import com.example.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;

@Controller
public class OverviewController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerService customerService;
    @GetMapping("/admin/overview")
    public String overview(Model model) {
        double totalSales = 0.0;
        int totalProducts = 0 ;
        int totalOrders = 0;
        int totalCustomers = 0;
        for (Order item : orderService.getAllOrders()) {
            totalOrders++;
            BigDecimal totalAmount = item.getTotalAmount();
            if (totalAmount != null) {
                totalSales += totalAmount.doubleValue();
            }
            totalProducts+=totalProducts(item);
        }
        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("totalSales", totalSales);
        model.addAttribute("totalOrders", totalOrders);

        return "Admin/Overview/index";
    }
    public int totalProducts(Order order) {
        int totalProducts = 0;
        for(OrderProduct item : order.getOrderProducts()) {
            totalProducts+= item.getQuantity();
        }
        return totalProducts;
    }
}
