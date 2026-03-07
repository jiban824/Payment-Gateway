package com.example.Payment.Gateway.entity;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "transactions")
@Getter
@Setter
public class Transaction {
     @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Double amount;

    private String currency;

    private String paymentMethod;

    private String referenceId;

    private String status;

    private String merchantId;

    private Instant createdAt;
}
