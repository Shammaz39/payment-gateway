package com.paymentgateway.paymentprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class PaymentProcessorApplication {
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
        System.out.println("Default JVM TimeZone: " + java.util.TimeZone.getDefault().getID());
        SpringApplication.run(PaymentProcessorApplication.class, args);
    }
}
