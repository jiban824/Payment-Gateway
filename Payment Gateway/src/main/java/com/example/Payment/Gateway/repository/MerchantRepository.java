package com.example.Payment.Gateway.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Payment.Gateway.entity.Merchant;

public interface MerchantRepository extends JpaRepository<Merchant, String> {
}