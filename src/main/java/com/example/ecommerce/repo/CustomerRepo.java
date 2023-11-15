package com.example.ecommerce.repo;

import com.example.ecommerce.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepo  extends JpaRepository<Customer, Long>{
}
