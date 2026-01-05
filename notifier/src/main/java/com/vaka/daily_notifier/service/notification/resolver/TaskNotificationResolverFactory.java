package com.vaka.daily_notifier.service.notification.resolver;

import com.vaka.daily_notifier.domain.TaskType;

public class TaskNotificationResolverFactory {
    public NotificationResolver getNotificationResolver(TaskType taskType) {
        NotificationResolver resolver;

        if ("singular".equalsIgnoreCase(taskType.getName())) {
            resolver = new SingularTaskNotificationResolver();
        } else if ("repetitive".equalsIgnoreCase(taskType.getName())) {
            resolver = new RepetitiveTaskNotificationResolver();
        } else if ("regular".equalsIgnoreCase(taskType.getName())) {
            resolver = new RegularTaskNotificationResolver();
        } else {
            throw new IllegalArgumentException("Unknown task type: " + taskType.getName());
        }

        return resolver;
    }
}
