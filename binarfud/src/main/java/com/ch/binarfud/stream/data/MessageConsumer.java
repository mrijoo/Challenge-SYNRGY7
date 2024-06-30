package com.ch.binarfud.stream.data;

import java.util.UUID;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.ch.binarfud.service.UserService;
import com.ch.binarfud.stream.data.register.UserRegisterDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MessageConsumer {
    private ObjectMapper objectMapper;

    private UserService userService;

    public MessageConsumer(ObjectMapper objectMapper, UserService userService) {
        this.objectMapper = objectMapper;
        this.userService = userService;
    }

    @KafkaListener(topics = "user-register")
    public void consume(String message) {
        try {
            UUID userId = UUID.fromString(objectMapper.readValue(message, UserRegisterDto.class).getUserId());
            userService.addUser(userId);
        } catch (Exception e) {
            log.error("Failed to consume message from Kafka", e);
        }
    }
}