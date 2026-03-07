package com.example.Payment.Gateway.service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.Payment.Gateway.dto.TransactionRequest;
import com.example.Payment.Gateway.entity.IdempotencyRecord;
import com.example.Payment.Gateway.entity.Merchant;
import com.example.Payment.Gateway.entity.Transaction;
import com.example.Payment.Gateway.repository.IdempotencyRepository;
import com.example.Payment.Gateway.repository.MerchantRepository;
import com.example.Payment.Gateway.repository.TransactionRepository;
import com.example.Payment.Gateway.validation.BusinessValidation;
import com.example.Payment.Gateway.validation.EntityValidation;
import com.example.Payment.Gateway.validation.RiskValidation;

@Service
public class TransactionService {
    private final MerchantRepository merchantRepository;
    private final TransactionRepository transactionRepository;
    private final IdempotencyRepository idempotencyRepository;

    private final EntityValidation entityValidation;
    private final BusinessValidation businessValidation;
    private final RiskValidation riskValidation;

    public TransactionService(
        MerchantRepository merchantRepository,
        TransactionRepository transactionRepository,
        IdempotencyRepository idempotencyRepository,
        EntityValidation entityValidation,
        BusinessValidation businessValidation,
        RiskValidation riskValidation
    ) {
    this.merchantRepository = merchantRepository;
    this.transactionRepository = transactionRepository;
    this.idempotencyRepository = idempotencyRepository;
    this.entityValidation = entityValidation;
    this.businessValidation = businessValidation;
    this.riskValidation = riskValidation;
    }

    public ResponseEntity<?> process(
        TransactionRequest request,
        String apiKey,
        String idempotencyKey
    ) {

        // 1️⃣ Idempotency Check
        Optional<IdempotencyRecord> existing =
                idempotencyRepository.findById(idempotencyKey);

        if (existing.isPresent()) {
            return ResponseEntity.ok("Duplicate request - already processed");
        }

        // 2️⃣ Fetch Merchant
        Merchant merchant = merchantRepository.findById(apiKey).orElse(null);

        // 3️⃣ Entity Validation
        entityValidation.validate(merchant);

        // 4️⃣ Business Validation
        businessValidation.validate(request);

        // 5️⃣ Risk Validation
        riskValidation.validate(merchant);

        // 6️⃣ Create Transaction
        Transaction txn = new Transaction();
        txn.setAmount(request.getAmount());
        txn.setCurrency(request.getCurrency());
        txn.setPaymentMethod(request.getPaymentMethod());
        txn.setReferenceId(request.getReferenceId());
        txn.setMerchantId(apiKey);
        txn.setStatus("PROCESSING");
        txn.setCreatedAt(Instant.now());

        transactionRepository.save(txn);

        // 7️⃣ Store Idempotency
        IdempotencyRecord record = new IdempotencyRecord();
        record.setIdempotencyKey(idempotencyKey);
        record.setTransactionId(txn.getId());
        idempotencyRepository.save(record);

        // 8️⃣ Success Response
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);

        Map<String, Object> data = new HashMap<>();
        data.put("transaction_id", txn.getId());
        data.put("status", txn.getStatus());
        data.put("amount", txn.getAmount());
        data.put("currency", txn.getCurrency());

        response.put("data", data);
        response.put("timestamp", Instant.now());

        return ResponseEntity.status(201).body(response);
    }
}
