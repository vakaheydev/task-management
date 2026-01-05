package com.vaka.daily_notifier.service.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.vaka.daily_notifier.domain.Task;
import com.vaka.daily_notifier.domain.TaskNotification;
import com.vaka.daily_notifier.domain.TaskType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ApiClientService {
    private RestClient restClient;
    private ObjectMapper objectMapper;

    public ApiClientService(RestClient restClient, ObjectMapper objectMapper) {
        this.restClient = restClient;
        this.objectMapper = objectMapper;
    }

    public Optional<Long> getTelegramIdByScheduleId(int scheduleId) {
        String response;
        try {
            response = restClient.get()
                    .uri("/schedule/" + scheduleId)
                    .retrieve()
                    .body(String.class);
        } catch (Exception exception) {
            log.error("Exception while getting schedule: {}", exception.getClass().getSimpleName());
            return Optional.empty();
        }

        if (response == null || response.isBlank()) {
            return Optional.empty();
        }

        try {
            JsonNode node = objectMapper.readTree(response);
            int userId = node.get("userId").asInt();
            return getTelegramIdByUserId(userId);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse schedule response: {}", e.getMessage());
            return Optional.empty();
        }
    }

    private Optional<Long> getTelegramIdByUserId(int userId) {
        String response;
        try {
            response = restClient.get()
                    .uri("/user/" + userId)
                    .retrieve()
                    .body(String.class);
        } catch (Exception exception) {
            log.error("Exception while getting user: {}", exception.getClass().getSimpleName());
            return Optional.empty();
        }

        if (response == null || response.isBlank()) {
            return Optional.empty();
        }

        try {
            JsonNode node = objectMapper.readTree(response);
            return Optional.of(node.get("telegramId").asLong());
        } catch (JsonProcessingException e) {
            log.error("Failed to parse user response: {}", e.getMessage());
            return Optional.empty();
        }

    }

    public List<TaskType> getTaskTypes() {
        String response;
        try {
            response = restClient.get()
                    .uri("/task_type")
                    .retrieve()
                    .body(String.class);
        } catch (Exception exception) {
            log.error("Exception while getting task types: {}", exception.getClass().getSimpleName());
            return Collections.emptyList();
        }

        if (response == null || response.isBlank()) {
            return Collections.emptyList();
        }

        try {
            return objectMapper.readValue(response,
                    TypeFactory.defaultInstance().constructCollectionType(List.class, TaskType.class));
        } catch (JsonProcessingException e) {
            log.error("Failed to parse task types response: {}", e.getMessage());
            return Collections.emptyList();
        }
    }


    public List<Task> getTasks() {
        String response;
        try {
            response = restClient.get()
                    .uri("/task")
                    .retrieve()
                    .body(String.class);
        } catch (Exception exception) {
            log.error("Exception while getting tasks: {}", exception.getClass().getSimpleName());
            return Collections.emptyList();
        }

        if (response == null || response.isBlank()) {
            return Collections.emptyList();
        }

        try {
            return objectMapper.readValue(response,
                    TypeFactory.defaultInstance().constructCollectionType(List.class, Task.class));
        } catch (JsonProcessingException e) {
            log.error("Failed to parse tasks response: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public TaskNotification getTaskNotificationByTaskId(int taskId) {
        String response = "";
        try {
            response = restClient.get()
                    .uri("/task/" + taskId + "/notification")
                    .retrieve()
                    .body(String.class);
        } catch (HttpClientErrorException.NotFound exception) {
            return null;
        } catch (Exception exception) {
            log.error("Exception while getting task notification: {}", exception.getClass().getSimpleName());
            return null;
        }

        if (response == null || response.isBlank()) {
            return null;
        }

        try {
            return objectMapper.readValue(response, TaskNotification.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse task notification response: {}", e.getMessage());
            return null;
        }
    }
}
