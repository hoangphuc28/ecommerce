package com.example.ecommerce.controller.admin;

import com.example.ecommerce.model.User;
import com.example.ecommerce.service.CustomUserDetailsService;
import com.example.ecommerce.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/admin/users")
    public String getCustomers(Model model) {
        model.addAttribute("users", userDetailsService.getAllUsers());
        return "Admin/User/index";

    }

    @GetMapping("/admin/users/add")
    public String addCustomers(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("checkError", "");
        return "Admin/User/add";
    }

    @PostMapping("/admin/users/add")
    public String addCustomers(Model model, User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (userDetailsService.isUserExist(user.getEmail())) {
            model.addAttribute("user", user);
            model.addAttribute("checkError", "Email already exists!");
            return "Admin/User/add";
        }
        user.setBlock(false);
        userDetailsService.NewUser(user);
        return "redirect:/admin/users";
    }
    @PostMapping("/admin/user/block/{str}")
    public String blockUser(@PathVariable("str") String str) {
       var user =  userDetailsService.getUserByString(str);
       user.setBlock(true);
       userDetailsService.updateUser(user);
        return "redirect:/admin/users";
    }
    @PostMapping("/admin/user/unblock/{str}")
    public String unblockUser(@PathVariable("str") String str) {
        var user =  userDetailsService.getUserByString(str);
        user.setBlock(false);
        userDetailsService.updateUser(user);
        return "redirect:/admin/users";
    }

}
