package com.binar.notifier.stream;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.binar.notifier.service.MailService;
import com.binar.notifier.stream.data.email.EmailSendOtpDto;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class MessageConsumer {
    private MailService mailService;

    private ObjectMapper objectMapper;

    public MessageConsumer(MailService mailService, ObjectMapper objectMapper) {
        this.mailService = mailService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "email-verification")
    public void consume(String message) {
        try {
            EmailSendOtpDto sendOtpDto = objectMapper.readValue(message, EmailSendOtpDto.class);
            mailService.sendVerificationEmail(sendOtpDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "email-forgot-password")
    public void consumeResetPasswordEmail(String message) {
        try {
            EmailSendOtpDto sendOtpDto = objectMapper.readValue(message, EmailSendOtpDto.class);
            mailService.sendResetPasswordEmail(sendOtpDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
