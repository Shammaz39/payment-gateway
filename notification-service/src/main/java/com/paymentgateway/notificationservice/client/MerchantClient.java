package com.paymentgateway.notificationservice.client;


import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MerchantClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public String fetchWebhookUrl(String email) {

        return restTemplate.getForObject(
                "http://localhost:8081/api/v1/merchants/webhooks?email={email}",
                String.class,
                email
        );
    }
}
