package com.example.Payment.Gateway.validation;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.example.Payment.Gateway.exception.CustomException;
import com.example.Payment.Gateway.entity.Merchant;

@Component
public class EntityValidation {
    public void validate(Merchant merchant) {
        if (merchant == null)
            throw new CustomException(HttpStatus.FORBIDDEN, "ENTITY_MERCHANT_NOT_FOUND", "Merchant not found");

        if (!merchant.isActive())
            throw new CustomException(HttpStatus.FORBIDDEN, "ENTITY_MERCHANT_INACTIVE", "Merchant inactive");

        if (!merchant.isKycApproved())
            throw new CustomException(HttpStatus.FORBIDDEN, "ENTITY_KYC_NOT_APPROVED", "KYC not approved");
    }

}
