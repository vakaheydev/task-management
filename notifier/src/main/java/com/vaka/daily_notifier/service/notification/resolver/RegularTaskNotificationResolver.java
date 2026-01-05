package com.vaka.daily_notifier.service.notification.resolver;

import com.vaka.daily_notifier.domain.Task;
import com.vaka.daily_notifier.domain.TaskNotification;

import java.time.LocalDateTime;

import static com.vaka.daily_notifier.domain.util.DateTimeUtil.*;

public class RegularTaskNotificationResolver implements NotificationResolver {
    @Override
    public boolean shouldNotify(Task task, TaskNotification taskNotification) {
        if (taskNotification == null) {
            return true;
        }

        LocalDateTime lastNotifiedAt = taskNotification.getLastNotifiedAt();
        int daysFromLastNotified = getDaysFrom(lastNotifiedAt);

        if (daysFromLastNotified < 1) {
            return false;
        }

        int daysToDeadLine = getDaysTo(task.getDeadline());
        long minutesToDeadLine = getMinutesTo(task.getDeadline());

        return daysToDeadLine == 1
                || minutesToDeadLine == 5
                || minutesToDeadLine == 0;
    }
}
