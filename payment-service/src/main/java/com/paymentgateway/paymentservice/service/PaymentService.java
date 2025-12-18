package com.paymentgateway.paymentservice.service;

import com.paymentgateway.paymentservice.dto.PaymentRequest;
import com.paymentgateway.paymentservice.dto.PaymentResponse;


public interface PaymentService {
    PaymentResponse createPayment(String merchantId, PaymentRequest request, String idempotencyKey);
}
