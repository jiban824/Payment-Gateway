package com.example.Payment.Gateway.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Payment.Gateway.dto.TransactionRequest;
import com.example.Payment.Gateway.service.TransactionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transactions")
    public Object createTransaction(
            @RequestHeader(value = "X-Api-Key", required = false) String apiKey,
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey,
            @Valid @RequestBody(required = true) TransactionRequest request
    ) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new com.example.Payment.Gateway.exception.CustomException(
                    org.springframework.http.HttpStatus.BAD_REQUEST, 
                    "MISSING_API_KEY", 
                    "Required header 'X-Api-Key' is missing"
            );
        }
        
        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            throw new com.example.Payment.Gateway.exception.CustomException(
                    org.springframework.http.HttpStatus.BAD_REQUEST, 
                    "MISSING_IDEMPOTENCY_KEY", 
                    "Required header 'Idempotency-Key' is missing"
            );
        }
        
        if (request == null) {
            throw new com.example.Payment.Gateway.exception.CustomException(
                    org.springframework.http.HttpStatus.BAD_REQUEST, 
                    "MISSING_REQUEST_BODY", 
                    "Request body is missing"
            );
        }
        
        return transactionService.process(request, apiKey, idempotencyKey);
    }
}
