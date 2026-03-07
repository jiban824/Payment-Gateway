package com.example.Payment.Gateway.event;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEvent {
    private String eventId;
    private String eventType;
    private String transactionId;
    private String status;
    private Double amount;
    private String currency;
    private String paymentMethod;
    private String referenceId;
    private String merchantId;
    private Instant timestamp;
    private String requestId;
    
    @Builder.Default
    private boolean critical = false;
    
    public boolean isCritical() {
        return critical;
    }}