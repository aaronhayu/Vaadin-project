package com.example.shop.backend.repositories;

import com.example.shop.backend.models.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    Page<Transaction> findByStatus(Transaction.TransactionStatus status, Pageable pageable);
    List<Transaction> findByStatus(Transaction.TransactionStatus status);
    long countByStatus(Transaction.TransactionStatus status);
    @Query("SELECT t FROM Transaction t WHERE " +
            "LOWER(t.transactionId) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(t.product) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(t.buyerEmail) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Transaction> searchTransactions(@Param("searchTerm") String searchTerm, Pageable pageable);
}
