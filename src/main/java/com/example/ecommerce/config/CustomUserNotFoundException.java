package com.example.ecommerce.config;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserNotFoundException extends UsernameNotFoundException {
    public CustomUserNotFoundException(String message) {
        super(message);
    }
}
