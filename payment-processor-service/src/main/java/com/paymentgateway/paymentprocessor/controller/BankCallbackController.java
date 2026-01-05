package com.paymentgateway.paymentprocessor.controller;


import com.paymentgateway.commonlib.event.PaymentProcessedEvent;
import com.paymentgateway.paymentprocessor.dto.BankCallbackRequest;
import com.paymentgateway.paymentprocessor.entity.Transaction;
import com.paymentgateway.paymentprocessor.entity.TransactionStatus;
import com.paymentgateway.paymentprocessor.kafka.PaymentProcessedProducer;
import com.paymentgateway.paymentprocessor.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/processor")
public class BankCallbackController {

    private final TransactionRepository transactionRepository;
    private final PaymentProcessedProducer paymentProcessedProducer;


    public BankCallbackController(
            TransactionRepository transactionRepository,
            PaymentProcessedProducer paymentProcessedProducer
    ) {
        this.transactionRepository = transactionRepository;
        this.paymentProcessedProducer = paymentProcessedProducer;
    }

    @PostMapping("/callback")
    @Transactional
    public void handleCallback(@RequestBody BankCallbackRequest callback) {

        System.out.println("================================");
        System.out.println("🏦 Bank Callback Received");
        System.out.println("Transaction ID: " + callback.getTransactionId());
        System.out.println("Status: " + callback.getStatus());

        Transaction tx = transactionRepository
                .findById(UUID.fromString(callback.getTransactionId()))
                .orElseThrow(() ->
                        new RuntimeException("Transaction not found"));

        // 🔐 IDEMPOTENCY CHECK
        if (tx.getStatus() != TransactionStatus.PENDING) {
            System.out.println("⚠️ Callback ignored (already finalized)");
            return;
        }

        // ✅ FINAL STATUS UPDATE
        if ("SUCCESS".equalsIgnoreCase(callback.getStatus())) {
            tx.setStatus(TransactionStatus.SUCCESS);
        } else {
            tx.setStatus(TransactionStatus.FAILED);
        }

        transactionRepository.save(tx);

        // 📢 PUBLISH FINAL EVENT
        PaymentProcessedEvent event =
                new PaymentProcessedEvent(tx.getId(),tx.getMerchantId(), tx.getStatus().name());

        paymentProcessedProducer.publish(event);

        System.out.println("✅ Transaction finalized & event published");
        System.out.println("================================");
    }
}