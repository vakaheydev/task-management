package com.vaka.daily_notifier.service.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaka.daily_notifier.domain.Notification;
import com.vaka.daily_notifier.domain.TaskNotification;
import com.vaka.daily_notifier.service.client.ApiClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class KafkaNotificationService {
    private final ApiClientService api;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public KafkaNotificationService(ApiClientService api, KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.api = api;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendNotifications(List<Notification> notifications) {
        for (Notification notification : notifications) {
            try {
                String json = objectMapper.writeValueAsString(notification);
                kafkaTemplate.send("telegram", json).get();
                try {
                    api.updateNotificationByTaskId(notification.getTaskId(), TaskNotification.builder().lastNotifiedAt(notification.getTimestamp()).build());
                } catch (Exception e) {
                    log.error("Failed to update notification for task {}: {}", notification.getTaskId(), e.getMessage());
                }
            } catch (JsonProcessingException e) {
                log.error("Failed to serialize notification to json: {}", e.getMessage());
            } catch (Exception e) {
                log.error("Failed to send notification to kafka: {}", e.getMessage());
            }
        }
    }
}