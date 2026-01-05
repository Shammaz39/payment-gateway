package com.paymentgateway.paymentservice.service;

import com.paymentgateway.commonlib.event.PaymentInitiatedEvent;
import com.paymentgateway.paymentservice.dto.PaymentRequest;
import com.paymentgateway.paymentservice.dto.PaymentResponse;
import com.paymentgateway.paymentservice.entity.PaymentLog;
import com.paymentgateway.paymentservice.entity.Transaction;
import com.paymentgateway.paymentservice.enums.TransactionStatus;
import com.paymentgateway.paymentservice.exception.InValidException;
import com.paymentgateway.paymentservice.kafka.PaymentEventProducer;
import com.paymentgateway.paymentservice.repository.PaymentLogRepository;
import com.paymentgateway.paymentservice.repository.TransactionRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Service
public class PaymentServiceImpli implements PaymentService {

    private final TransactionRepository transactionRepository;
    private final PaymentLogRepository logRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final PaymentEventProducer paymentEventProducer;

    public PaymentServiceImpli(
            TransactionRepository transactionRepository,
            PaymentLogRepository logRepository,
            PaymentEventProducer paymentEventProducer,
            RedisTemplate<String, Object> redisTemplate
    ) {
        this.transactionRepository = transactionRepository;
        this.logRepository = logRepository;
        this.redisTemplate = redisTemplate;
        this.paymentEventProducer = paymentEventProducer;
    }

    // =========================================================
// CREATE PAYMENT (IDEMPOTENT – CORRECT)
// =========================================================
    @Override
    public PaymentResponse createPayment(
            String merchantId,
            PaymentRequest request,
            String idempotencyKey
    ) {

        validateRequest(merchantId, request, idempotencyKey);

        String lockKey = "payment:lock:" + merchantId + ":" + idempotencyKey;
        String responseKey = "payment:response:" + merchantId + ":" + idempotencyKey;

        // 1️⃣ FIRST: Check idempotency response (works even for sequential calls)
        String existingTxnId =
                (String) redisTemplate.opsForValue().get(responseKey);

        if (existingTxnId != null) {
            Transaction tx = transactionRepository
                    .findById(UUID.fromString(existingTxnId))
                    .orElseThrow();

            return buildResponse(tx);
        }

        // 2️⃣ Acquire lock (protect concurrent requests)
        Boolean lockAcquired = redisTemplate.opsForValue()
                .setIfAbsent(lockKey, "LOCKED", Duration.ofMinutes(5));

        if (!Boolean.TRUE.equals(lockAcquired)) {
            throw new InValidException("Duplicate payment request in progress");
        }

        try {
            // 3️⃣ Create transaction
            Transaction transaction = Transaction.builder()
                    .merchantId(merchantId)
                    .amount(request.getAmount())
                    .currency(request.getCurrency())
                    .status(TransactionStatus.PENDING)
                    .build();

            transaction = transactionRepository.save(transaction);

            // 4️⃣ Log
            logRepository.save(
                    PaymentLog.builder()
                            .transactionId(transaction.getId())
                            .message("Payment created with PENDING status of amount " + transaction.getCurrency() + " " + transaction.getAmount())
                            .build()
            );

            // 5️⃣ Cache transactionId for idempotency (SOURCE OF TRUTH)
            redisTemplate.opsForValue().set(
                    responseKey,
                    transaction.getId().toString(),
                    Duration.ofHours(24)
            );

            // 6️⃣ Publish Kafka event
            PaymentInitiatedEvent event = PaymentInitiatedEvent.builder()
                    .transactionId(transaction.getId())
                    .merchantId(merchantId)
                    .amount(transaction.getAmount())
                    .currency(transaction.getCurrency())
                    .build();

            paymentEventProducer.publishPaymentInitiated(event);

            return buildResponse(transaction);

        } finally {
            // 7️⃣ Always release lock
            redisTemplate.delete(lockKey);
        }
    }

    // =========================================================
    // GET PAYMENTS (LATEST FIRST, FILTERABLE)
    // =========================================================
    @Override
    public List<PaymentResponse> getPayments(
            String merchantId,
            TransactionStatus status,
            Double minAmount,
            Double maxAmount,
            LocalDate fromDate,
            LocalDate toDate
    ) {

        List<Transaction> transactions;
        LocalDateTime fromDateTime =
                (fromDate != null) ? fromDate.atStartOfDay() : null;

        LocalDateTime toDateTime =
                (toDate != null) ? toDate.atTime(23, 59, 59) : null;

        if (fromDate != null && toDate != null) {
            transactions = transactionRepository.filterWithDates(
                    merchantId, fromDateTime, toDateTime );
        } else {
            transactions = transactionRepository.filterWithoutDates(
                    merchantId, status, minAmount, maxAmount );
        }

        return transactions.stream()
                .map(this::buildResponse)
                .toList();
    }

    // =========================================================
    // HELPERS
    // =========================================================
    private PaymentResponse buildResponse(Transaction tx) {
        return PaymentResponse.builder()
                .transactionId(tx.getId().toString())
                .amount(tx.getAmount())
                .currency(tx.getCurrency())
                .status(tx.getStatus().name())
                .createdAt(tx.getCreatedAt())
                .build();
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
