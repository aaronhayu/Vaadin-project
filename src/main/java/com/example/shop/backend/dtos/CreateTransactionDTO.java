package com.example.shop.backend.dtos;

import java.math.BigDecimal;

public record CreateTransactionDTO(
        String product,
        BigDecimal amount,
        String buyerName,
        String buyerEmail
) {
    // Compact constructor for validation
    public CreateTransactionDTO {
        if (product == null || product.isEmpty()) {
            throw new IllegalArgumentException("Product cannot be null or empty");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (buyerName == null || buyerName.isEmpty()) {
            throw new IllegalArgumentException("Buyer name cannot be null or empty");
        }
        if (buyerEmail == null || buyerEmail.isEmpty()) {
            throw new IllegalArgumentException("Buyer email cannot be null or empty");
        }
    }
}