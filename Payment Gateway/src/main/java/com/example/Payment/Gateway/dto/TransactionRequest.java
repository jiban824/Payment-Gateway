package com.example.Payment.Gateway.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class TransactionRequest {
       @NotNull
    private Double amount;

    @NotBlank
    private String currency;

    @NotBlank
    private String paymentMethod;

    @NotBlank
    private String referenceId;

    @Valid
    private Customer customer;
}
