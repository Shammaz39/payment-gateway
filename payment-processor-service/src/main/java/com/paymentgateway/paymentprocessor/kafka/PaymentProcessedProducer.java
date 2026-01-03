package com.paymentgateway.paymentprocessor.kafka;

import com.paymentgateway.commonlib.event.PaymentProcessedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PaymentProcessedProducer {
    private static final String TOPIC = "payment.processed";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public PaymentProcessedProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(PaymentProcessedEvent event) {
        kafkaTemplate.send(
                TOPIC,
                String.valueOf(event.getTransactionId()),
                event
        );
    }
}
