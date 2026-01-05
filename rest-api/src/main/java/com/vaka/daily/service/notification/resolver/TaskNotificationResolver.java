package com.vaka.daily.service.notification.resolver;

import com.vaka.daily.domain.Task;
import com.vaka.daily.domain.TaskNotification;
import com.vaka.daily.exception.notfound.TaskNotFoundException;
import com.vaka.daily.service.domain.TaskNotificationService;
import org.springframework.stereotype.Component;

@Component
public class TaskNotificationResolver {
    private final TaskNotificationService taskNotificationService;

    public TaskNotificationResolver(TaskNotificationService taskNotificationService) {
        this.taskNotificationService = taskNotificationService;
    }

    public boolean shouldNotify(Task task) {
        TaskNotification taskNotification;

        try {
            taskNotification = taskNotificationService.getByTaskId(task.getId());
        } catch (TaskNotFoundException ex) {
            return true;
        }

        NotificationResolver resolver;

        if (isTaskType(task, "singular")) {
            resolver = new SingularTaskNotificationResolver(taskNotificationService);
        } else if (isTaskType(task, "repetitive")) {
            resolver = new RepetitiveTaskNotificationResolver();
        } else if (isTaskType(task, "regular")) {
            resolver = new RegularTaskNotificationResolver();
        } else {
            throw new IllegalArgumentException("Unknown task type: " + task.getTaskType());
        }

        return resolver.shouldNotify(task, taskNotification);
    }

    public boolean isTaskType(Task task, String taskTypeName) {
        return task.getTaskType().getName().equals(taskTypeName);
    }
}
