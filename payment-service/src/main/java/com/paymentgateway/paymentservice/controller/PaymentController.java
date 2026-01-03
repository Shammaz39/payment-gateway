package com.paymentgateway.paymentservice.controller;

import com.paymentgateway.paymentservice.dto.PaymentRequest;
import com.paymentgateway.paymentservice.dto.PaymentResponse;
import com.paymentgateway.paymentservice.enums.TransactionStatus;
import com.paymentgateway.paymentservice.service.PaymentService;
import com.paymentgateway.paymentservice.util.JwtUtil;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final JwtUtil jwtUtil;

    // Constructor injection → recommended
    public PaymentController(PaymentService paymentService, JwtUtil jwtUtil) {
        this.paymentService = paymentService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public PaymentResponse createPayment(
            Authentication authentication,
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @RequestBody PaymentRequest request
    ) {
        String merchantId = authentication.getName(); //

        return paymentService.createPayment(merchantId, request, idempotencyKey);
    }


    @GetMapping
    public List<PaymentResponse> getPayments(
            Authentication authentication,

            @RequestParam(required = false) TransactionStatus status,
            @RequestParam(required = false) Double minAmount,
            @RequestParam(required = false) Double maxAmount,

            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate fromDate,

            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate toDate
    ) {

        String merchantId = authentication.getName();

        return paymentService.getPayments(
                merchantId,
                status,
                minAmount,
                maxAmount,
                fromDate,
                toDate
        );
    }
}
