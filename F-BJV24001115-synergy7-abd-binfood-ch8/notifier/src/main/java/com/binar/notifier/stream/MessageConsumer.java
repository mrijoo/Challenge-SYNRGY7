package com.binar.notifier.stream;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.binar.notifier.service.FirebaseService;
import com.binar.notifier.service.MailService;
import com.binar.notifier.stream.data.email.EmailSendOtpDto;
import com.binar.notifier.stream.data.firebase.SendNotificationDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MessageConsumer {
    private ObjectMapper objectMapper;

    private MailService mailService;

    private FirebaseService firebaseService;

    public MessageConsumer (ObjectMapper objectMapper, MailService mailService, FirebaseService firebaseService) {
        this.objectMapper = objectMapper;
        this.mailService = mailService;
        this.firebaseService = firebaseService;
    }

    @KafkaListener(topics = "email-verification")
    public void consume(String message) {
        try {
            EmailSendOtpDto sendOtpDto = objectMapper.readValue(message, EmailSendOtpDto.class);
            mailService.sendVerificationEmail(sendOtpDto);
        } catch (Exception e) {
            log.error("Error consuming email verification", e);
        }
    }

    @KafkaListener(topics = "email-forgot-password")
    public void consumeResetPasswordEmail(String message) {
        try {
            EmailSendOtpDto sendOtpDto = objectMapper.readValue(message, EmailSendOtpDto.class);
            mailService.sendResetPasswordEmail(sendOtpDto);
        } catch (Exception e) {
            log.error("Error consuming reset password email", e);
        }
    }

    @KafkaListener(topics = "send-notification")
    public void consumeSendNotification(String message) {
        try {
            SendNotificationDto sendNotification = objectMapper.readValue(message, SendNotificationDto.class);
            firebaseService.sendNotification(sendNotification);
        } catch (Exception e) {
            log.error("Error consuming send notification", e);
        }
    }
}
