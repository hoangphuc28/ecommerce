package com.example.ecommerce.repo;

import com.example.ecommerce.model.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodRepo extends JpaRepository<PaymentMethod, Long> {
    // Additional query methods can be added here if needed
}
