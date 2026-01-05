package com.vaka.daily_notifier.service.notification;

import com.vaka.daily_notifier.domain.Notification;
import com.vaka.daily_notifier.domain.Task;
import com.vaka.daily_notifier.domain.TaskNotification;
import com.vaka.daily_notifier.domain.TaskType;
import com.vaka.daily_notifier.service.client.ApiClientService;
import com.vaka.daily_notifier.service.notification.format.FormatNotificationService;
import com.vaka.daily_notifier.service.notification.resolver.TaskNotificationResolverFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class NotificationService {
    private final ApiClientService api;
    private final KafkaNotificationService kafka;
    private final TaskNotificationResolverFactory notificationResolverFactory;
    private final FormatNotificationService formatNotificationService;

    public NotificationService(ApiClientService api, KafkaNotificationService kafka,
                               FormatNotificationService formatNotificationService) {
        this.api = api;
        this.kafka = kafka;
        this.notificationResolverFactory = new TaskNotificationResolverFactory();
        this.formatNotificationService = formatNotificationService;
    }

    public void notifyUsers() {
        List<Task> tasks = api.getTasks();
        List<Notification> notifications = new ArrayList<>();
        List<TaskType> taskTypes = api.getTaskTypes();
        Map<Integer, TaskType> taskTypeCache = new HashMap<>();
        for (TaskType taskType : taskTypes) {
            taskTypeCache.put(taskType.getId(), taskType);
        }

        for (Task task : tasks) {
            TaskNotification taskNotification = api.getTaskNotificationByTaskId(task.getId());
            TaskType taskType = taskTypeCache.get(task.getTaskTypeId());
            if (taskType == null) {
                log.error("Unknown task type: {}, it will be skipped", task.getTaskTypeId());
                continue;
            }
            Notification notification = resolveNotification(task, taskType, taskNotification);
            if (notification != null) {
                notifications.add(notification);
            }
        }

        kafka.sendNotifications(notifications);
        log.info("{} notifications sent to kafka", notifications.size());
    }


    private Notification resolveNotification(Task task, TaskType taskType, TaskNotification taskNotification) {
        if (notificationResolverFactory.getNotificationResolver(taskType).shouldNotify(task, taskNotification)) {
            return resolveTelegramNotification(task, taskType);
        }

        return null;
    }

    // Write a function that creates a telegram notification

    private Notification resolveTelegramNotification(Task task, TaskType taskType) {
        int scheduleId = task.getScheduleId();
        Optional<Long> optTgId = api.getTelegramIdByScheduleId(scheduleId);

        if (optTgId.isEmpty()) {
            return null;
        }

        long telegramId = optTgId.get();

        return Notification.builder()
                .chanel("telegram")
                .chanelUserId(Long.toString(telegramId))
                .message(formatNotificationService.formatTaskForNotification(task, taskType))
                .timestamp(LocalDateTime.now())
                .build();
    }
}
