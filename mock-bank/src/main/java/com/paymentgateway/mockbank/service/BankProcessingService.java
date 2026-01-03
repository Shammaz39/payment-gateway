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

        int amount = request.getAmount().intValue();

        // 1️⃣ Amount < 5000 → immediate SUCCESS
        if (amount < 5000) {

            response.setStatus("SUCCESS");

            new Thread(() -> {
                sendCallback(request, "SUCCESS");
            }).start();

        }
        // 2️⃣ Amount between 5000 and 10000 → SUCCESS after 3 sec
        else if (amount <= 10000) {

            response.setStatus("PENDING");

            new Thread(() -> {
                try {
                    Thread.sleep(3000);
                    sendCallback(request, "SUCCESS");
                } catch (Exception e) {
                    sendCallback(request, "FAILED");
                }
            }).start();

        }
        // 3️⃣ Amount > 10000 → RANDOM SUCCESS / FAILED after 3 sec
        else {

            response.setStatus("PENDING");

            new Thread(() -> {
                try {
                    Thread.sleep(3000);

                    boolean success = Math.random() < 0.5;
                    sendCallback(request, success ? "SUCCESS" : "FAILED");

                } catch (Exception e) {
                    sendCallback(request, "FAILED");
                }
            }).start();
        }

        System.out.println(
                "Mock Bank initial response -> txId="
                        + response.getTransactionId()
                        + ", status=" + response.getStatus()
        );

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
