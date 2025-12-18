package com.paymentgateway.paymentservice.service;

import com.paymentgateway.paymentservice.dto.PaymentRequest;
import com.paymentgateway.paymentservice.dto.PaymentResponse;
import com.paymentgateway.paymentservice.entity.PaymentLog;
import com.paymentgateway.paymentservice.entity.Transaction;
import com.paymentgateway.paymentservice.enums.TransactionStatus;
import com.paymentgateway.paymentservice.exception.InValidException;
import com.paymentgateway.paymentservice.repository.PaymentLogRepository;
import com.paymentgateway.paymentservice.repository.TransactionRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class PaymentServiceImpli implements PaymentService {
    private final TransactionRepository transactionRepository;
    private final PaymentLogRepository logRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public PaymentServiceImpli(TransactionRepository transactionRepository, PaymentLogRepository logRepository, RedisTemplate<String, Object> redisTemplate) {
        this.transactionRepository = transactionRepository;
        this.logRepository = logRepository;
        this.redisTemplate = redisTemplate;
    }
    @Override
    public PaymentResponse createPayment(
            String merchantId,
            PaymentRequest request,
            String idempotencyKey
    ) {

        // ---------- 1️⃣ Basic validation ----------
        validateRequest(merchantId, request, idempotencyKey);

        String lockKey = "payment:lock:" + merchantId + ":" + idempotencyKey;
        String responseKey = "payment:response:" + merchantId + ":" + idempotencyKey;

// 1️⃣ Try acquiring lock
        Boolean lockAcquired = redisTemplate.opsForValue()
                .setIfAbsent(lockKey, "LOCKED", Duration.ofMinutes(5));

        if (Boolean.FALSE.equals(lockAcquired)) {
            PaymentResponse cached =
                    (PaymentResponse) redisTemplate.opsForValue().get(responseKey);

            if (cached != null) {
                return cached;
            }

            throw new InValidException("Duplicate payment request");
        }

        try {
            // ---------- 3️⃣ Persist transaction ----------
            Transaction transaction = Transaction.builder()
                    .merchantId(merchantId)
                    .amount(request.getAmount())
                    .currency(request.getCurrency())
                    .status(TransactionStatus.PENDING)
                    .build();

            transaction = transactionRepository.save(transaction);

            // ---------- 4️⃣ Persist payment log ----------
            PaymentLog log = PaymentLog.builder()
                    .transactionId(transaction.getId())
                    .message("Payment created with PENDING status")
                    .build();

            logRepository.save(log);

            // ---------- 5️⃣ Build response ----------
            PaymentResponse response = PaymentResponse.builder()
                    .transactionId(transaction.getId().toString())
                    .status(transaction.getStatus().name())
                    .amount(transaction.getAmount())
                    .currency(transaction.getCurrency())
                    .build();

            // ---------- 6️⃣ Cache final response (24h) ----------
            redisTemplate.opsForValue()
                    .set(responseKey, response, Duration.ofHours(24));

            return response;

        } catch (Exception ex) {
            // Important: remove lock if something failed
            redisTemplate.delete(lockKey);
            throw ex;
        }
    }

    private void validateRequest(
            String merchantId,
            PaymentRequest request,
            String idempotencyKey
    ) {

        if (merchantId == null || merchantId.isBlank()) {
            throw new InValidException("Invalid merchant token");
        }

        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            throw new InValidException("Idempotency-Key is required");
        }

        if (request == null) {
            throw new InValidException("Payment request is required");
        }

        if (request.getAmount() == null || request.getAmount() <= 0) {
            throw new InValidException("Amount must be greater than 0");
        }

        if (request.getAmount() > 1_000_000) {
            throw new InValidException("Amount exceeds limit");
        }

        if (request.getCurrency() == null || request.getCurrency().isBlank()) {
            throw new InValidException("Currency is required");
        }

        List<String> allowedCurrencies = List.of("INR", "USD", "EUR");
        if (!allowedCurrencies.contains(request.getCurrency())) {
            throw new InValidException("Unsupported currency");
        }
    }
}