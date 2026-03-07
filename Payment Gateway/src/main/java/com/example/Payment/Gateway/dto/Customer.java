package com.example.Payment.Gateway.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Customer {
    @Email
    private String email;

    @NotBlank
    private String phone;
}
