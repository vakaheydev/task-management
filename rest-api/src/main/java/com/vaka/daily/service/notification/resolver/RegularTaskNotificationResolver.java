package com.vaka.daily.service.notification.resolver;

import com.vaka.daily.domain.Task;
import com.vaka.daily.domain.TaskNotification;

import java.time.LocalDateTime;

import static com.vaka.daily.domain.util.DateTimeUtil.*;

public class RegularTaskNotificationResolver implements NotificationResolver {
    @Override
    public boolean shouldNotify(Task task, TaskNotification taskNotification) {
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
