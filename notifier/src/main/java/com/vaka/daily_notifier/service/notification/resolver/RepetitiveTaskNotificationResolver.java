package com.vaka.daily_notifier.service.notification.resolver;

import com.vaka.daily_notifier.domain.Task;
import com.vaka.daily_notifier.domain.TaskNotification;

import java.time.LocalDateTime;

import static com.vaka.daily_notifier.domain.util.DateTimeUtil.getDaysFrom;
import static com.vaka.daily_notifier.domain.util.DateTimeUtil.getDaysTo;
import static com.vaka.daily_notifier.domain.util.TaskUtil.isTaskDeadLineLater;

public class RepetitiveTaskNotificationResolver implements NotificationResolver {
    @Override
    public boolean shouldNotify(Task task, TaskNotification taskNotification) {
        if (taskNotification == null) {
            return true;
        }

        LocalDateTime lastNotifiedAt = taskNotification.getLastNotifiedAt();

        if (isTaskDeadLineLater(task)) {
            int daysFromLastNotified = getDaysFrom(lastNotifiedAt);

            if (daysFromLastNotified < 1) {
                return false;
            }

            int daysToDeadLine = getDaysTo(task.getDeadline());
            return daysToDeadLine == 1;
        } else {
            int daysFromLastNotified = getDaysFrom(lastNotifiedAt);

            return daysFromLastNotified >= 1;
        }
    }
}
