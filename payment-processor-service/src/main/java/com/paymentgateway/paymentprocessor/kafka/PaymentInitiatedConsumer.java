package com.paymentgateway.paymentprocessor.kafka;


import com.paymentgateway.commonlib.event.PaymentInitiatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PaymentInitiatedConsumer {

    @KafkaListener(
            topics = "payment.initiated",
            groupId = "payment-log"
    )
    public void consume(PaymentInitiatedEvent event) {
        System.out.println("================================");
        System.out.println("✅ Payment Event Received by Group Id payment-log");
        System.out.println("Transaction ID: " + event.getTransactionId());
        System.out.println("Amount: " + event.getAmount());
        System.out.println("Currency: " + event.getCurrency());
        System.out.println("================================");


    }
}
