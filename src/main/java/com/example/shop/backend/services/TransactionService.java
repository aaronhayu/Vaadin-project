package com.example.shop.backend.services;

import com.example.shop.backend.dtos.CreateTransactionDTO;
import com.example.shop.backend.models.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface TransactionService {
    void createTransaction(CreateTransactionDTO transaction);

    Transaction getTransactionById(UUID transactionId);

    Page<Transaction> getTransactions(Pageable pageable);

    void updateTransactionStatus(UUID transactionId, Transaction.TransactionStatus status);

    void deleteTransaction(UUID transactionId);

    Page<Transaction> getTransactionsByStatus(String status, Pageable pageable);

    long countTransactions();

    long countTransactionsByStatus(String status);

    String generateCsvData(String status);

    Page<Transaction> searchTransactions(String searchTerm, Pageable pageable);
}
