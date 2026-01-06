package com.vaka.daily_notifier.service.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.vaka.daily_notifier.domain.AuthResponse;
import com.vaka.daily_notifier.domain.Task;
import com.vaka.daily_notifier.domain.TaskNotification;
import com.vaka.daily_notifier.domain.TaskType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ApiClientService {
    private RestClient restClient;
    private ObjectMapper objectMapper;
    private volatile String token;
    private volatile Instant expiresAt;

    private final String USERNAME;
    private final String PASSWORD;

    public ApiClientService(RestClient restClient,
                            ObjectMapper objectMapper,
                            @Value("${app.connection.credentials.username}") String login,
                            @Value("${app.connection.credentials.password}") String password) {
        this.restClient = restClient;
        this.objectMapper = objectMapper;
        this.USERNAME = login;
        this.PASSWORD = password;
    }

    public void authorizeIfNeeded() {
        if (token != null && Instant.now().isBefore(expiresAt.minusSeconds(30))) {
            return;
        }

        AuthResponse response;
        String url = "/auth/login";
        log.info("Authorizing to REST API using POST {}", url);
        try {
            response = restClient.post()
                    .uri(url)
                    .body("{\"username\":\"" + this.USERNAME + "\", \"password\":\"" + this.PASSWORD + "\"}")
                    .retrieve()
                    .body(AuthResponse.class);
        } catch (Exception exception) {
            log.error("Exception while authorizing: {}", exception.getClass().getSimpleName());
            throw new RuntimeException("Failed to authorize to REST API");
        }

        if (response == null) {
            throw new RuntimeException("Failed to authorize to REST API");
        }

        String token = response.accessToken();
        Instant expiresAt = response.expiresAt();
        this.token = token;
        this.expiresAt = expiresAt;
    }

    public Optional<Long> getTelegramIdByScheduleId(int scheduleId) {
        String response = getRequest("/schedule/" + scheduleId, String.class);
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
        String response = getRequest("/user/" + userId, String.class);
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
        String response = getRequest("/task_type", String.class);
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
        String response = getRequest("/task", String.class);
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

    public TaskNotification updateNotificationByTaskId(int taskId, TaskNotification taskNotification) {
        String response = putRequest("/task/" + taskId + "/notification", taskNotification, String.class);
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

    public TaskNotification getTaskNotificationByTaskId(int taskId) {
        String response = getRequest("/task/" + taskId + "/notification", String.class);
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

    public <T> T getRequest(String uri, Class<T> responseType) {
        authorizeIfNeeded();
        log.info("Making GET request to {}", uri);
        try {
            return restClient.get()
                    .uri(uri)
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .body(responseType);
        } catch (Exception exception) {
            String exName = exception.getClass().getSimpleName();
            if (!exName.equals("NotFound")) {
                log.error("Exception while making GET request to {}: {}", uri, exName);
            }
            return null;
        }
    }

    public <T> T putRequest(String uri, Object requestBody, Class<T> responseType) {
        authorizeIfNeeded();
        log.info("Making PUT request to {}", uri);
        try {
            return restClient.put()
                    .uri(uri)
                    .header("Authorization", "Bearer " + token)
                    .body(requestBody)
                    .retrieve()
                    .body(responseType);
        } catch (Exception exception) {
            String exName = exception.getClass().getSimpleName();
            if (!exName.equals("NotFound")) {
                log.error("Exception while making PUT request to {}: {}", uri, exName);
            }
            return null;
        }
    }
}
