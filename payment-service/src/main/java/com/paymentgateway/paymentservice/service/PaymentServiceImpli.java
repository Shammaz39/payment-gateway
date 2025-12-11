package com.paymentgateway.paymentservice.service;

import com.paymentgateway.paymentservice.dto.PaymentRequest;
import com.paymentgateway.paymentservice.dto.PaymentResponse;
import com.paymentgateway.paymentservice.entity.Payment;
import com.paymentgateway.paymentservice.entity.PaymentLog;
import com.paymentgateway.paymentservice.entity.Transaction;
import com.paymentgateway.paymentservice.enums.TransactionStatus;
import com.paymentgateway.paymentservice.repository.PaymentLogRepository;
import com.paymentgateway.paymentservice.repository.TransactionRepository;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpli implements PaymentService{
    private final TransactionRepository transactionRepository;
    private final PaymentLogRepository logRepository;

    public PaymentServiceImpli(TransactionRepository transactionRepository, PaymentLogRepository logRepository) {
        this.transactionRepository = transactionRepository;
        this.logRepository = logRepository;
    }

    @Override
    public PaymentResponse createPayment(String merchantId, PaymentRequest req) {

        // 1️⃣ Save Transaction
        Transaction tx = Transaction.builder()
                .amount(req.getAmount())
                .currency(req.getCurrency())
                .merchantId(merchantId)
                .status(TransactionStatus.PENDING)
                .build();

        tx = transactionRepository.save(tx);

        // 2️⃣ Add Log
        PaymentLog log = PaymentLog.builder()
                .transactionId(tx.getId())
                .message("Payment created with PENDING status")
                .build();

        logRepository.save(log);

        // 3️⃣ Return Response
        return PaymentResponse.builder()
                .transactionId(tx.getId().toString())
                .status(tx.getStatus().name())
                .amount(tx.getAmount())
                .currency(tx.getCurrency())
                .build();
    }
}
