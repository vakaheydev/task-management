package com.vaka.daily_notifier.service.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaka.daily_notifier.domain.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class KafkaNotificationService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public KafkaNotificationService(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendNotifications(List<Notification> notifications) {
        for (Notification notification : notifications) {
            try {
                String json = objectMapper.writeValueAsString(notification);
                kafkaTemplate.send("telegram", json);
            } catch (JsonProcessingException e) {
                log.error("Failed to serialize notification to json: {}", e.getMessage());
            }
        }
    }
}