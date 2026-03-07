package com.example.Payment.Gateway.validation;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.example.Payment.Gateway.entity.Merchant;
import com.example.Payment.Gateway.exception.CustomException;
import com.example.Payment.Gateway.repository.TransactionRepository;

@Component
public class RiskValidation {
    private final TransactionRepository transactionRepository;

    public RiskValidation(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void validate(Merchant merchant) {
        if (merchant == null || transactionRepository == null) return;

        Instant fiveMinutesAgo = Instant.now().minus(5, ChronoUnit.MINUTES);

        long count = transactionRepository.countRecentTransactions(merchant.getApiKey(), fiveMinutesAgo);

        if (count > 10)
            throw new CustomException(HttpStatus.TOO_MANY_REQUESTS,
                    "RISK_VELOCITY_EXCEEDED",
                    "Too many transactions in short time");
    }

}
