package com.binar.security.stream;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.binar.security.stream.data.email.EmailSendOtpDto;
import com.binar.security.stream.data.register.UserRegisterDto;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class MessageProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    private ObjectMapper objectMapper;

    public MessageProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendUserRegister(String userId) {
        try {
            UserRegisterDto userRegisterDto = new UserRegisterDto();
            userRegisterDto.setUserId(userId);
            String message = objectMapper.writeValueAsString(userRegisterDto);
            kafkaTemplate.send("user-register", message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendVerificationEmail(String to, String username, String otp) {
        EmailSendOtpDto emailSendOtpDto = new EmailSendOtpDto();
        emailSendOtpDto.setTo(to);
        emailSendOtpDto.setUsername(username);
        emailSendOtpDto.setOtp(otp);

        try {
            String message = objectMapper.writeValueAsString(emailSendOtpDto);
            kafkaTemplate.send("email-verification", message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendForgotPasswordEmail(String to, String username, String otp) {
        EmailSendOtpDto emailSendOtpDto = new EmailSendOtpDto();
        emailSendOtpDto.setTo(to);
        emailSendOtpDto.setUsername(username);
        emailSendOtpDto.setOtp(otp);

        try {
            String message = objectMapper.writeValueAsString(emailSendOtpDto);
            kafkaTemplate.send("email-forgot-password", message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}