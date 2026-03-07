package com.example.Payment.Gateway.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import com.example.Payment.Gateway.dto.FieldError;
import com.example.Payment.Gateway.dto.TransactionRequest;

public class SchemaValidation {
        private static final Set<String> VALID_PAYMENT_METHODS = Set.of("upi", "credit_card", "debit_card", "net_banking");
    private static final Set<String> VALID_CURRENCIES = Set.of("INR");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.+\\-]+@[\\w\\-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[1-9]\\d{9,14}$");

    public void validate(TransactionRequest request, String requestId) {
        List<FieldError> errors = new ArrayList<>();

        if (request.getAmount() == null) {
            errors.add(FieldError.builder().field("amount").rule("required").message("is required").build());
        } else if (request.getAmount() <= 0) {
            errors.add(FieldError.builder().field("amount").rule("positive").message("must be greater than 0").build());
        }

        if (request.getCurrency() == null || request.getCurrency().isBlank()) {
            errors.add(FieldError.builder().field("currency").rule("required").message("is required").build());
        } else if (!VALID_CURRENCIES.contains(request.getCurrency())) {
            errors.add(FieldError.builder().field("currency").rule("enum").message("must be one of: INR").build());
        }

        if (request.getPaymentMethod() == null || request.getPaymentMethod().isBlank()) {
            errors.add(FieldError.builder().field("payment_method").rule("required").message("is required").build());
        } else if (!VALID_PAYMENT_METHODS.contains(request.getPaymentMethod())) {
            errors.add(FieldError.builder().field("payment_method").rule("enum")
                    .message("must be one of: upi, credit_card, debit_card, net_banking").build());
        }

        if (request.getReferenceId() == null || request.getReferenceId().isBlank()) {
            errors.add(FieldError.builder().field("reference_id").rule("required").message("is required").build());
        }

        if (request.getCustomer() == null) {
            errors.add(FieldError.builder().field("customer").rule("required").message("is required").build());
        } else {
            if (request.getCustomer().getEmail() == null || request.getCustomer().getEmail().isBlank()) {
                errors.add(FieldError.builder().field("customer.email").rule("required").message("is required").build());
            } else if (!EMAIL_PATTERN.matcher(request.getCustomer().getEmail()).matches()) {
                errors.add(FieldError.builder().field("customer.email").rule("format").message("invalid email format").build());
            }

            if (request.getCustomer().getPhone() == null || request.getCustomer().getPhone().isBlank()) {
                errors.add(FieldError.builder().field("customer.phone").rule("required").message("is required").build());
            } else if (!PHONE_PATTERN.matcher(request.getCustomer().getPhone()).matches()) {
                errors.add(FieldError.builder().field("customer.phone").rule("format").message("invalid phone format").build());
            }
        }

        if (!errors.isEmpty()) {
            throw new jakarta.validation.ValidationException(
                    "Required field '" + errors.get(0).getField() + "' " + errors.get(0).getMessage()
            );
        }
    }
}
