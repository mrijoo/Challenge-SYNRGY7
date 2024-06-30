package com.ch.binarfud.stream.data;

import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.ch.binarfud.stream.data.firebase.SendNotificationDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MessageProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    private ObjectMapper objectMapper;

    public MessageProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendNotification(List<String> token, String title, String body) {
        try {
            SendNotificationDto sendNotificationDto = new SendNotificationDto();
            sendNotificationDto.setTokens(token);
            sendNotificationDto.setTitle(title);
            sendNotificationDto.setBody(body);

            kafkaTemplate.send("send-notification", objectMapper.writeValueAsString(sendNotificationDto));
        } catch (Exception e) {
            log.error("Failed to send message to Kafka", e);
        }
    }
}
