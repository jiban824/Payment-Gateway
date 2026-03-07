package com.example.Payment.Gateway.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "idempotency_records")
@Getter
@Setter

public class IdempotencyRecord {
    @Id
    @Column(name = "idempotency_key")
    private String idempotencyKey;

    private String transactionId;
}
