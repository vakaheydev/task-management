package com.vaka.daily.service.notification;

import com.vaka.daily.domain.Task;
import com.vaka.daily.domain.User;
import com.vaka.daily.domain.TaskNotification;
import com.vaka.daily.exception.notfound.TaskNotFoundException;
import com.vaka.daily.service.domain.TaskNotificationService;
import com.vaka.daily.service.notification.format.FormatTaskService;
import com.vaka.daily.service.notification.resolver.TaskNotificationResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NotificationService {
    @Value("${telegram.enabled}")
    private boolean telegramEnabled;
    private final TelegramService telegramService;
    private final FormatTaskService formatTaskService;
    private final TaskNotificationService taskNotificationService;
    private final TaskNotificationResolver taskNotificationResolver;

    @Autowired
    public NotificationService(TelegramService telegramService, FormatTaskService formatTaskService,
                               TaskNotificationService taskNotificationService,
                               TaskNotificationResolver taskNotificationResolver) {
        this.telegramService = telegramService;
        this.formatTaskService = formatTaskService;
        this.taskNotificationService = taskNotificationService;
        this.taskNotificationResolver = taskNotificationResolver;
    }

    @Transactional
    public void notifyUsers() {
        List<Task> tasksForNotification = taskNotificationService.getTasksForNotification();
        Map<User, List<Task>> userTasksMap = tasksForNotification.stream()
                .collect(Collectors.groupingBy((task -> task.getSchedule().getUser())));

        int notified = 0;

        for (var entry : userTasksMap.entrySet()) {
            User user = entry.getKey();
            List<Task> tasks = entry.getValue();

            boolean wasNotified = notifyUserByTelegram(user, tasks);
            if (wasNotified) {
                notified++;
            }
        }
        log.info("{} users were notified", notified);
    }

    /**
     * Notify user by telegram
     * @param user
     * @param tasks
     * @return if user was notified
     */
    private boolean notifyUserByTelegram(User user, List<Task> tasks) {
        if (user.getTelegramId() == null) {
            return false;
        }

        boolean wasNotified = false;

        for (Task task : tasks) {
            if (taskNotificationResolver.shouldNotify(task)) {
                String msg = formatTaskService.formatTaskForNotification(task);
                boolean messageSent = telegramService.sendMessage(user.getTelegramId(), msg);

                if (messageSent) {
                    wasNotified = true;
                    setTaskWasNotified(task);
                }

            }
        }

        return wasNotified;
    }

    private void setTaskWasNotified(Task task) {
        try {
            TaskNotification taskNotification = taskNotificationService.getByTaskId(task.getId());
            taskNotification.setLastNotifiedAt(LocalDateTime.now());

            taskNotificationService.updateById(taskNotification.getId(), taskNotification);
        } catch (TaskNotFoundException ex) {
            TaskNotification taskNotification = new TaskNotification();
            taskNotification.setTask(task);
            taskNotification.setLastNotifiedAt(LocalDateTime.now());

            taskNotificationService.create(taskNotification);
        }
    }
}
