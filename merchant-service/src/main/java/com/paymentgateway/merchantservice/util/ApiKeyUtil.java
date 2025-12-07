package com.paymentgateway.merchantservice.util;

import java.security.SecureRandom;
import java.util.Base64;

public class ApiKeyUtil {
    public static String generateApiKey() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32]; // 256-bit key
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
