package com.paymentgateway.merchantservice;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class MerchantServiceApplication {
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
        System.out.println("Default JVM TimeZone: " + java.util.TimeZone.getDefault().getID());
        SpringApplication.run(MerchantServiceApplication.class, args);
    }
}
