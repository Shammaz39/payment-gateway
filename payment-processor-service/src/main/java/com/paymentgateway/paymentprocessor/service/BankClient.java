package com.paymentgateway.paymentprocessor.service;

import com.paymentgateway.mockbank.dto.BankPaymentRequest;
import com.paymentgateway.mockbank.dto.BankPaymentResponse;
import com.paymentgateway.paymentprocessor.entity.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
@Service
public class BankClient {

    @Value("${processor.service.url}")
    private String processorServiceUrl;

    @Value("${mockbank.service.url}")
    private String mockbankServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public BankPaymentResponse initiatePayment(Transaction tx) {

        String processCallBackUrl = processorServiceUrl+ "/callback";
        String mockBankPayUrl = mockbankServiceUrl+ "/pay";

        BankPaymentRequest request = new BankPaymentRequest();
        request.setTransactionId(String.valueOf(tx.getId()));
        request.setAmount(tx.getAmount());
        request.setCallbackUrl(
                processCallBackUrl
        );

        return restTemplate.postForObject(
                mockBankPayUrl,
                request,
                BankPaymentResponse.class
        );
    }
}
