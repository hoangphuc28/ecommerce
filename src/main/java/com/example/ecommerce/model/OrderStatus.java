package com.example.ecommerce.model;

import lombok.Data;

public enum OrderStatus {
    SHIPPED,
    DELIVERING,
    CANCELED,
    PAYMENT_PENDING
}
