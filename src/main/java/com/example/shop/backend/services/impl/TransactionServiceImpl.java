package com.example.shop.backend.services.impl;

import com.example.shop.backend.dtos.CreateTransactionDTO;
import com.example.shop.backend.models.Transaction;
import com.example.shop.backend.repositories.TransactionRepository;
import com.example.shop.backend.services.TransactionService;
import com.example.shop.backend.utils.Util;
import com.helger.commons.csv.CSVWriter;
import com.vaadin.flow.router.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of TransactionService interface for managing transactions.
 */
@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repository;

    @Override
    public void createTransaction(CreateTransactionDTO transaction) {
        // Generate a transaction ID (8 random digits)
        String transactionId = Util.generateEightDigitNumber();

        // Create a new Transaction entity
        Transaction newTransaction = Transaction.builder()
                .transactionId(transactionId)
                .product(transaction.product())
                .amount(transaction.amount())
                .buyerName(transaction.buyerName())
                .buyerEmail(transaction.buyerEmail())
                .status(Transaction.TransactionStatus.PENDING)
                .date(LocalDateTime.now())
                .build();

        // Save the transaction to the database
        repository.save(newTransaction);
    }

    @Override
    public Transaction getTransactionById(UUID transactionId) {
        // Retrieve transaction by ID from repository
        var transaction = repository.findById(transactionId);

        // Throw NotFoundException if transaction is not found
        if (transaction.isEmpty()) {
            throw new NotFoundException("Transaction not found");
        }

        return transaction.get();
    }

    @Override
    public Page<Transaction> getTransactions(Pageable pageable) {
        // Retrieve all transactions with pagination
        return repository.findAll(pageable);
    }

    @Override
    public void updateTransactionStatus(UUID transactionId, Transaction.TransactionStatus status) {
        // Retrieve transaction by ID from repository
        var transaction = repository.findById(transactionId);

        // Throw NotFoundException if transaction is not found
        if (transaction.isEmpty()) {
            throw new NotFoundException("Transaction not found");
        }

        // Update transaction status and save to repository
        transaction.get().setStatus(status);
        repository.save(transaction.get());
    }

    @Override
    public void deleteTransaction(UUID transactionId) {
        // Retrieve transaction by ID from repository
        var transaction = repository.findById(transactionId);

        // Throw NotFoundException if transaction is not found
        if (transaction.isEmpty()) {
            throw new NotFoundException("Transaction not found");
        }

        // Delete transaction from repository
        repository.delete(transaction.get());
    }

    @Override
    public Page<Transaction> getTransactionsByStatus(String status, Pageable pageable) {
        // Convert status string to TransactionStatus enum
        Transaction.TransactionStatus transactionStatus = getTransactionStatus(status);

        // Retrieve transactions by status from repository with pagination
        return repository.findByStatus(transactionStatus, pageable);
    }

    @Override
    public long countTransactions() {
        // Count total number of transactions in repository
        return repository.count();
    }

    @Override
    public long countTransactionsByStatus(String status) {
        // Convert status string to TransactionStatus enum
        Transaction.TransactionStatus transactionStatus = getTransactionStatus(status);

        // Count number of transactions by status in repository
        return repository.countByStatus(transactionStatus);
    }

    @Override
    public String generateCsvData(String status) {
        List<Transaction> transactions;

        // Retrieve transactions based on status from repository
        if ("ALL".equals(status)) {
            transactions = repository.findAll();
        } else {
            Transaction.TransactionStatus transactionStatus = Transaction.TransactionStatus.valueOf(status.toUpperCase());
            transactions = repository.findByStatus(transactionStatus);
        }

        // Generate CSV data from transactions
        StringWriter writer = new StringWriter();
        try (CSVWriter csvWriter = new CSVWriter(writer)) {
            csvWriter.writeNext("Transaction ID", "Product", "Amount", "Buyer Email", "Status", "Date");
            for (Transaction transaction : transactions) {
                csvWriter.writeNext(transaction.getTransactionId(),
                        transaction.getProduct(),
                        String.valueOf(transaction.getAmount()),
                        transaction.getBuyerEmail(),
                        transaction.getStatus().name(),
                        transaction.getDate().toString());
            }
        } catch (Exception e) {
            // Handle exception when generating CSV data
            throw new HttpServerErrorException(HttpStatusCode.valueOf(500), "Unable to download file");
        }
        return writer.toString();
    }

    @Override
    public Page<Transaction> searchTransactions(String searchTerm, Pageable pageable) {
        // Search transactions in repository based on searchTerm with pagination
        return repository.searchTransactions(searchTerm, pageable);
    }

    /**
     * Converts a status string to TransactionStatus enum.
     *
     * @param status Status string to convert
     * @return Corresponding TransactionStatus enum
     * @throws IllegalArgumentException if status string is not a valid TransactionStatus
     */
    public Transaction.TransactionStatus getTransactionStatus(String status) {
        try {
            return Transaction.TransactionStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Handle the case where the status string is not a valid enum value
            throw new IllegalArgumentException("Invalid transaction status: " + status);
        }
    }
}
