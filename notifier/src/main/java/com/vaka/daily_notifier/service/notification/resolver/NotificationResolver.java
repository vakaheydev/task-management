package com.vaka.daily_notifier.service.notification.resolver;

import com.vaka.daily_notifier.domain.Task;
import com.vaka.daily_notifier.domain.TaskNotification;

public interface NotificationResolver {
    boolean shouldNotify(Task task, TaskNotification taskNotification);
}
