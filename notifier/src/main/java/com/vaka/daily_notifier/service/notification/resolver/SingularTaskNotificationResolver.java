package com.vaka.daily_notifier.service.notification.resolver;

import com.vaka.daily_notifier.domain.Task;
import com.vaka.daily_notifier.domain.TaskNotification;

import java.time.LocalDateTime;

import static com.vaka.daily_notifier.domain.util.DateTimeUtil.getDaysFrom;

public class SingularTaskNotificationResolver implements NotificationResolver {
    @Override
    public boolean shouldNotify(Task task, TaskNotification taskNotification) {
        if (taskNotification == null) {
            return true;
        }

        LocalDateTime lastNotifiedAt = taskNotification.getLastNotifiedAt();
        int daysFromLastNotified = getDaysFrom(lastNotifiedAt);

        return daysFromLastNotified >= 1;
    }
}
