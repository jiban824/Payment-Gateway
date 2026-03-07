package com.example.Payment.Gateway.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private String status;
    private String errorCode;
    private String message;
    private String validationType;
    private List<FieldError> errors;
    private String requestId;

    public static TransactionResponse error(String errorCode, String message, String validationType, 
                                             List<FieldError> errors, String requestId) {
        return TransactionResponse.builder()
                .status("ERROR")
                .errorCode(errorCode)
                .message(message)
                .validationType(validationType)
                .errors(errors)
                .requestId(requestId)
                .build();
    }
}
