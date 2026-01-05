package com.paymentgateway.notificationservice.kafka;


import com.paymentgateway.commonlib.event.PaymentProcessedEvent;
import com.paymentgateway.notificationservice.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentProcessedConsumer {

    private final NotificationService notificationService;

    public PaymentProcessedConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(
            topics = "payment.processed",
            groupId = "notification-group"
    )
    public void consume(PaymentProcessedEvent event) {

        System.out.println("📣 Notification received");
        System.out.println("TX: " + event.getTransactionId());
        System.out.println("STATUS: " + event.getStatus());

        try {
            notificationService.handle(event);
        } catch (Exception e) {
            System.out.println("❌ Notification failed for TX" + event.getTransactionId()+
                    "/n"+ e);
        }
    }
}
