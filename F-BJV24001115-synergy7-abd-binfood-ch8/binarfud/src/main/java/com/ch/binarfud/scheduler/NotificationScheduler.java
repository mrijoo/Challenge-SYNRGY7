package com.ch.binarfud.scheduler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ch.binarfud.stream.data.MessageProducer;

@Component
public class NotificationScheduler {
    private final MessageProducer messageProducer;

    public NotificationScheduler(MessageProducer messageProducer) {
        this.messageProducer = messageProducer;
    }

    @Scheduled(cron = "0 0 12 * * *")
    // @Scheduled(cron = "*/10 * * * * *")
    public void sendPromotionNotification() {
        List<String> tokens = new ArrayList<>();
        tokens.add("eQXf2cVxtRCPAR5Wn6XE8v:APA91bFqpRsbmzII4lMMkVtIxEMHaAmsEkyIx9mdb0az7k5v8Gpm_y44pfISXvujxzMPgj0icXNKtviK3znxkcXyOkRI--rnp1pIQKAjEQ4jckVSxyhpUBjDPEynCPKyZ148LPVzc1Xs");

        messageProducer.sendNotification(tokens, "Promotion", "Get 50% discount for all items");
    }
}
