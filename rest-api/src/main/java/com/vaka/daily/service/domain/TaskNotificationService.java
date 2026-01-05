package com.vaka.daily.service.domain;

import com.vaka.daily.domain.Task;
import com.vaka.daily.domain.TaskNotification;
import com.vaka.daily.service.abstraction.CommonService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TaskNotificationService extends CommonService<TaskNotification> {
    TaskNotification getByTaskId(Integer taskId);
    @Transactional
    List<Task> getTasksForNotification();
}
