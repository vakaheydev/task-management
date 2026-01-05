package com.vaka.daily.service.notification.resolver;

import com.vaka.daily.domain.Task;
import com.vaka.daily.domain.TaskNotification;

public interface NotificationResolver {
    boolean shouldNotify(Task task, TaskNotification taskNotification);
}
