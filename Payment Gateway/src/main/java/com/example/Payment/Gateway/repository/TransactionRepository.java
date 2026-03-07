package com.example.Payment.Gateway.repository;

import java.time.Instant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.Payment.Gateway.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, String> {

    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.merchantId = :merchantId AND t.createdAt > :time")
    long countRecentTransactions(String merchantId, Instant time);
}