package com.paymentgateway.paymentservice.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class MerchantClient {

    @Value("${merchant.service.url}")
    private String merchantServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public boolean validateMerchant(String email, String apiKey) {
        String url = merchantServiceUrl + "/validate";

        Map<String, String> req = new HashMap<>();
        req.put("email", email);
        req.put("apiKey", apiKey);

        ResponseEntity<Boolean> response =
                restTemplate.postForEntity(url, req, Boolean.class);

        return response.getBody();
    }
}

