package com.paymentgateway.paymentprocessor.service;

import com.paymentgateway.mockbank.dto.BankPaymentRequest;
import com.paymentgateway.mockbank.dto.BankPaymentResponse;
import com.paymentgateway.paymentprocessor.entity.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
@Service
public class BankClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public BankPaymentResponse initiatePayment(Transaction tx) {

        BankPaymentRequest request = new BankPaymentRequest();
        request.setTransactionId(String.valueOf(tx.getId()));
        request.setAmount(tx.getAmount());
        request.setCallbackUrl(
                "http://localhost:8083/processor/callback"
        );

        return restTemplate.postForObject(
                "http://localhost:8084/bank/pay",
                request,
                BankPaymentResponse.class
        );
    }
}
