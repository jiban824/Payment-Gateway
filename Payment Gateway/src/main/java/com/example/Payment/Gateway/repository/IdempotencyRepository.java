package com.example.Payment.Gateway.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Payment.Gateway.entity.IdempotencyRecord;

public interface IdempotencyRepository extends JpaRepository<IdempotencyRecord, String> {
}