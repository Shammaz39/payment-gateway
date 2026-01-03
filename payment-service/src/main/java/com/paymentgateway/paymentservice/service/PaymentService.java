package com.paymentgateway.paymentservice.service;

import com.paymentgateway.paymentservice.dto.PaymentRequest;
import com.paymentgateway.paymentservice.dto.PaymentResponse;
import com.paymentgateway.paymentservice.enums.TransactionStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public interface PaymentService {
    PaymentResponse createPayment(String merchantId, PaymentRequest request, String idempotencyKey);
    List<PaymentResponse> getPayments(String merchantId, TransactionStatus status, Double minAmount, Double maxAmount, LocalDate fromDate, LocalDate toDate);
}
