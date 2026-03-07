package com.example.Payment.Gateway.validation;

import com.example.Payment.Gateway.dto.TransactionRequest;

public class ComplianceValidation {
    public boolean isFlagged(TransactionRequest request) {
        return request.getAmount() > 200000;
    }

}
