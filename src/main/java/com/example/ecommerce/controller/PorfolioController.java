package com.example.ecommerce.controller;

import com.example.ecommerce.model.User;
import com.example.ecommerce.service.CustomUserDetailsService;
import com.example.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class PorfolioController {
    @Autowired
    private CustomUserDetailsService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OrderService orderService;
    @GetMapping("/portfolio")
    public String showPortfolio(Principal principal, Model model) {
        var user = userService.getUserByString(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("err", "");
        return "Portfolio/index";
    }
    @PostMapping("/portfolio/change")
    public String changePortfolio(User user) {
        userService.updateInforUser(user);
        return "redirect:/portfolio";
    }
    @PostMapping("/portfolio/changepassword")
    public String changePassword(@RequestParam String password, @RequestParam String newPassword, Model model, Principal principal) {
        var user = userService.getUserByString(principal.getName());
        if(!passwordEncoder.matches(password, user.getPassword())) {
            model.addAttribute("user", user);
            model.addAttribute("err", "Password incorrect!");
            return "Portfolio/index";
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userService.updateUser(user);

        return "redirect:/portfolio";
    }
    @GetMapping("/orders")
    public String getOrders(Principal principal, Model model, @RequestParam(name = "search", required = false, defaultValue = "") String search) {
        if(principal!=null) {
            var user = userService.getUserByString(principal.getName());
            model.addAttribute("orders", orderService.getOrdersByUser(user));
            return "Portfolio/orders";

        }
        System.out.println(search);
        model.addAttribute("orders", orderService.getOrdersByEmailOrPhone(search));

        return "Portfolio/orders";
    }
    @GetMapping("/order/detail/{id}")
    public String getOrder( @PathVariable Long id, Model model) {
        var order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        return "Portfolio/order-detail";
    }

}
