package com.example.Payment.Gateway.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "merchants")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Merchant {
    @Id
    private String apiKey;

    private String name;

    private boolean active;

    private boolean kycApproved;

    private Double maxTransactionAmount;

    private String allowedPaymentMethods;
}
