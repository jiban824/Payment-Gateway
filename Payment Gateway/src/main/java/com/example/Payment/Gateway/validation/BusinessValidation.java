package com.example.Payment.Gateway.validation;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.example.Payment.Gateway.dto.TransactionRequest;
import com.example.Payment.Gateway.exception.CustomException;

@Component
public class BusinessValidation {
    public void validate(TransactionRequest request) {
        if (request == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "RULE_INVALID_REQUEST", "Request is null");
        }

        if (request.getAmount() == null || request.getAmount() <= 0) {
            throw new CustomException(HttpStatus.UNPROCESSABLE_ENTITY, "RULE_INVALID_AMOUNT", "Amount must be positive");
        }

        if (request.getAmount() > 100000) {
            throw new CustomException(HttpStatus.UNPROCESSABLE_ENTITY, "RULE_LIMIT_EXCEEDED", "Amount exceeds limit");
        }
    }

}
