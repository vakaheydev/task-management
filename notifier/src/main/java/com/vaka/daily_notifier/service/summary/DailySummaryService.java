package com.vaka.daily_notifier.service.summary;

import com.vaka.daily_notifier.domain.Task;
import com.vaka.daily_notifier.service.ai.AiSummaryService;
import com.vaka.daily_notifier.service.client.ApiClientService;
import com.vaka.daily_notifier.service.notification.KafkaNotificationService;
import com.vaka.daily_notifier.service.user.UserSelector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class DailySummaryService {

    private final UserSelector userSelector;
    private final ApiClientService apiClient;
    private final AiSummaryService aiSummaryService;
    private final KafkaNotificationService kafkaNotificationService;
    private final String morningPrompt;
    private final String eveningPrompt;

    private static final DateTimeFormatter DATETIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public DailySummaryService(
            UserSelector userSelector,
            ApiClientService apiClient,
            AiSummaryService aiSummaryService,
            KafkaNotificationService kafkaNotificationService,
            @Value("${app.daily-summary.morning-prompt}") String morningPrompt,
            @Value("${app.daily-summary.evening-prompt}") String eveningPrompt) {
        this.userSelector = userSelector;
        this.apiClient = apiClient;
        this.aiSummaryService = aiSummaryService;
        this.kafkaNotificationService = kafkaNotificationService;
        this.morningPrompt = morningPrompt;
        this.eveningPrompt = eveningPrompt;
    }

    public void sendMorningSummaries() {
        for (Integer userId : userSelector.selectUserIds()) {
            try {
                processMorning(userId);
            } catch (Exception e) {
                log.error("Failed to send morning summary for user {}: {}", userId, e.getMessage());
            }
        }
    }

    public void sendEveningSummaries() {
        for (Integer userId : userSelector.selectUserIds()) {
            try {
                processEvening(userId);
            } catch (Exception e) {
                log.error("Failed to send evening summary for user {}: {}", userId, e.getMessage());
            } finally {
                aiSummaryService.clearMemory(userId);
            }
        }
    }

    private void processMorning(Integer userId) {
        List<Task> incompleteTasks = getIncompleteTasks(userId);
        if (incompleteTasks.isEmpty()) {
            log.info("No incomplete tasks for user {}, skipping morning summary", userId);
            return;
        }

        Optional<Long> telegramId = apiClient.getTelegramIdByUserId(userId);
        if (telegramId.isEmpty()) {
            log.warn("No telegram ID found for user {}, skipping morning summary", userId);
            return;
        }

        String summary = aiSummaryService.generateSummary(userId, morningPrompt, Map.of(
                "current_datetime", LocalDateTime.now().format(DATETIME_FORMATTER),
                "tasks", aiSummaryService.formatTasks(incompleteTasks)
        ));

        kafkaNotificationService.sendSummary(summary, String.valueOf(telegramId.get()));
        log.info("Sent morning summary to user {} (telegramId={})", userId, telegramId.get());
    }

    private void processEvening(Integer userId) {
        List<Task> incompleteTasks = getIncompleteTasks(userId);

        Optional<Long> telegramId = apiClient.getTelegramIdByUserId(userId);
        if (telegramId.isEmpty()) {
            log.warn("No telegram ID found for user {}, skipping evening summary", userId);
            return;
        }

        List<Task> tomorrowTasks = incompleteTasks.stream()
                .filter(t -> t.getDeadline() != null &&
                        t.getDeadline().toLocalDate().equals(LocalDate.now().plusDays(1)))
                .toList();

        String summary = aiSummaryService.generateSummary(userId, eveningPrompt, Map.of(
                "current_datetime", LocalDateTime.now().format(DATETIME_FORMATTER),
                "tasks", aiSummaryService.formatTasks(incompleteTasks),
                "tomorrow_tasks", aiSummaryService.formatTasks(tomorrowTasks)
        ));

        kafkaNotificationService.sendSummary(summary, String.valueOf(telegramId.get()));
        log.info("Sent evening summary to user {} (telegramId={})", userId, telegramId.get());
    }

    private List<Task> getIncompleteTasks(Integer userId) {
        return apiClient.getTasksForUser(userId).stream()
                .filter(task -> Boolean.FALSE.equals(task.getStatus()))
                .toList();
    }
}
