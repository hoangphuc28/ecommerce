package com.example.ecommerce.controller;

import com.example.ecommerce.model.User;
import com.example.ecommerce.service.CustomUserDetailsService;
import com.example.ecommerce.service.EmailService;
import com.example.ecommerce.service.TokenService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.regex.Pattern;

@Controller
public class RegisterController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomUserDetailsService userService;
    @Autowired
    private  EmailService emailService;

    @Autowired
    private TokenService tokenService;
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "user/register";
    }

    @PostMapping("/register")
    public String registerUser(Model model, @ModelAttribute("user") User u) throws MessagingException, IOException {
        var token = tokenService.generateToken(u);
        if (userService.isUserExist(u.getEmail())) {
            model.addAttribute("user", u);
            model.addAttribute("checkError", "Email already exists!");
            return "user/register";
        }
        emailService.sendEmail(u.getEmail(), "Verify account", token);
        return "redirect:/login";
    }
    @GetMapping("/verify")
    public String verify(@RequestParam String token, Model model) {
        var user = tokenService.decryptToken(token);
        model.addAttribute("user", user);
        model.addAttribute("checkError", "");
        return "user/verify";
    }
    @PostMapping("/verify")
    public String completeRegister(User user, BindingResult res, Model model) {
        if(!user.getPassword().equals(user.getConfirmPassword())) {
            model.addAttribute("user", user);
            model.addAttribute("checkError", "Password does not match!");
            return "user/verify";
        }
        if(!validationPassword(user.getPassword())) {
            model.addAttribute("user", user);
            model.addAttribute("checkError", "Must Contain At Least 1 Uppercase 1 Lowercase And 1 Numeric Character Minimum 6 Characters");
            return "user/verify";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (!userService.isUserExist(user.getEmail())) {
            userService.NewUser(user);
            return "user/login";
        }
        userService.updateUser(user);
        return "user/login";
    }
    private Boolean validationPassword(String password) {
        String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$";

       return Pattern.matches(PASSWORD_REGEX, password);
    }
}
