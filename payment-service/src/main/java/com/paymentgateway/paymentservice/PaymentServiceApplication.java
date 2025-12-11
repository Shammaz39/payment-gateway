package com.paymentgateway.paymentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class PaymentServiceApplication {
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
        System.out.println("Default JVM TimeZone: " + java.util.TimeZone.getDefault().getID());
        SpringApplication.run(PaymentServiceApplication.class, args);
    }
}
