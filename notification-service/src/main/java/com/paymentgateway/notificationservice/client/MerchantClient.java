package com.paymentgateway.notificationservice.client;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MerchantClient {

    @Value("${merchant.service.url}")
    private String merchantServiceUrl;


    private final RestTemplate restTemplate = new RestTemplate();

    public String fetchWebhookUrl(String email) {

        String url = merchantServiceUrl + "/webhooks?email={email}";

        return restTemplate.getForObject(
                url,
                String.class,
                email
        );
    }
}
