package com.paymentgateway.mockbank.service;


import com.paymentgateway.mockbank.dto.BankPaymentRequest;
import com.paymentgateway.mockbank.dto.BankPaymentResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BankProcessingService {

    private final RestTemplate restTemplate = new RestTemplate();

    public BankPaymentResponse process(BankPaymentRequest request) {

        BankPaymentResponse response = new BankPaymentResponse();
        response.setTransactionId(request.getTransactionId());

        // SIMPLE & REALISTIC LOGIC
        if (request.getAmount().intValue() < 5000) {
            // immediate success
            response.setStatus("SUCCESS");
        } else {
            // async processing
            response.setStatus("PENDING");

            new Thread(() -> {
                try {
                    Thread.sleep(3000); // simulate bank delay
                    sendCallback(request, "SUCCESS");
                } catch (Exception e) {
                    sendCallback(request, "FAILED");
                }
            }).start();
        }
        System.out.println("Mock Bank response: "+ response.toString());

        return response;
    }

    private void sendCallback(BankPaymentRequest request, String status) {

        BankPaymentResponse callback = new BankPaymentResponse();
        callback.setTransactionId(request.getTransactionId());
        callback.setStatus(status);

        restTemplate.postForObject(
                request.getCallbackUrl(),
                callback,
                Void.class
        );
    }
}
