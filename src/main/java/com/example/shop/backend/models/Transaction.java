package com.example.shop.backend.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "transaction_id", nullable = false)
    private String transactionId;

    @Column(nullable = false)
    private String product;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "buyer_name", nullable = false)
    private String buyerName;

    @Column(name = "buyer_email", nullable = false)
    private String buyerEmail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @Column(nullable = false)
    private LocalDateTime date;

    public enum TransactionStatus {
        PENDING, COMPLETED, FAILED
    }
}
