package com.vaka.daily.service.notification.resolver;

import com.vaka.daily.domain.Task;
import com.vaka.daily.domain.TaskNotification;
import com.vaka.daily.service.domain.TaskNotificationService;

import java.time.LocalDateTime;

import static com.vaka.daily.domain.util.DateTimeUtil.getDaysFrom;

public class SingularTaskNotificationResolver implements NotificationResolver {
    private final TaskNotificationService taskNotificationService;

    public SingularTaskNotificationResolver(TaskNotificationService taskNotificationService) {
        this.taskNotificationService = taskNotificationService;
    }

    @Override
    public boolean shouldNotify(Task task, TaskNotification taskNotification) {

        LocalDateTime lastNotifiedAt = taskNotification.getLastNotifiedAt();
        int daysFromLastNotified = getDaysFrom(lastNotifiedAt);

        return daysFromLastNotified >= 1;
    }
}
