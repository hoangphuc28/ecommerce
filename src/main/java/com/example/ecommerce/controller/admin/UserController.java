package com.example.ecommerce.controller.admin;

import com.example.ecommerce.model.User;
import com.example.ecommerce.service.CustomUserDetailsService;
import com.example.ecommerce.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @GetMapping("/admin/users")
    public String getCustomers(Model model) {
        model.addAttribute("users", userDetailsService.getAllUsers());
        return "Admin/User/index";

    }

    @GetMapping("/admin/users/add")
    public String addCustomers(Model model) {
        model.addAttribute("user", new User());
        return "Admin/User/add";
    }

    @PostMapping("/admin/users/add")
    public String addCustomers(Model model, User user) {
        userDetailsService.NewUser(user);
        return "Admin/User/add";
    }

}
