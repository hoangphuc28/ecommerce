package com.example.ecommerce.service;

import com.example.ecommerce.model.PaymentMethod;
import com.example.ecommerce.repo.PaymentMethodRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentMethodService {

    private final PaymentMethodRepo paymentMethodRepository;

    @Autowired
    public PaymentMethodService(PaymentMethodRepo paymentMethodRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
    }

    public List<PaymentMethod> getAllPaymentMethods() {
        return paymentMethodRepository.findAll();
    }

    public PaymentMethod getPaymentMethodById(Long id) {
        return paymentMethodRepository.findById(id).orElse(null);
    }

    public PaymentMethod createPaymentMethod(PaymentMethod paymentMethod) {
        return paymentMethodRepository.save(paymentMethod);
    }

    public PaymentMethod updatePaymentMethod(Long id, PaymentMethod updatedPaymentMethod) {
        if (paymentMethodRepository.existsById(id)) {
            updatedPaymentMethod.setId(id);
            return paymentMethodRepository.save(updatedPaymentMethod);
        } else {
            // Handle not found scenario
            return null;
        }
    }

    public void deletePaymentMethod(Long id) {
        paymentMethodRepository.deleteById(id);
    }
}
