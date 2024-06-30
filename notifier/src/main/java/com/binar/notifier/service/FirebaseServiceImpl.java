package com.binar.notifier.service;

import com.binar.notifier.stream.data.firebase.SendNotificationDto;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FirebaseServiceImpl implements FirebaseService {

    @Override
    public void sendNotification(SendNotificationDto sendNotification) {
        List<String> tokens = sendNotification.getTokens();
        for (String token : tokens) {
            try {
                Message message = Message.builder()
                        .setToken(token)
                        .setNotification(Notification.builder()
                                .setTitle(sendNotification.getTitle())
                                .setBody(sendNotification.getBody())
                                .build())
                        .build();

                String response = FirebaseMessaging.getInstance().send(message);
                log.info("Sent message to token. Device token: {}, Response: {}", token, response);
            } catch (FirebaseMessagingException e) {
                log.error("Error sending notification to token {}: {}", token, e.getMessage());
            } catch (Exception e) {
                log.error("Unexpected error occurred while sending notification to token {}: {}", token,
                        e.getMessage());
            }
        }
    }
}
