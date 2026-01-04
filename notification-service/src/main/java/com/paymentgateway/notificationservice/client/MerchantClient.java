package com.paymentgateway.notificationservice.client;


import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MerchantClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public String fetchWebhookUrl(String merchantId) {

        return restTemplate.getForObject(
                "http://merchant-service:8081/internal/merchants/{id}/webhook",
                String.class,
                merchantId
        );
    }
}
