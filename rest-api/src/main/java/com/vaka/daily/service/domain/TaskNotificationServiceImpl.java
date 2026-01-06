package com.vaka.daily.service.domain;

import com.vaka.daily.domain.Task;
import com.vaka.daily.domain.TaskNotification;
import com.vaka.daily.exception.notfound.TaskNotFoundException;
import com.vaka.daily.exception.notfound.TaskNotificationNotFoundException;
import com.vaka.daily.repository.TaskNotificationRepository;
import com.vaka.daily.security.SecurityUtils;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskNotificationServiceImpl implements TaskNotificationService {
    private final TaskNotificationRepository repository;

    public TaskNotificationServiceImpl(TaskNotificationRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<TaskNotification> getAll() {
        if (SecurityUtils.currentUserHasRole("ADMIN")) {
            return repository.findAll();
        } else {
            throw new AuthorizationDeniedException("Access denied");
        }
    }

    @Override
    public TaskNotification getById(Integer id) {
        if (SecurityUtils.currentUserHasRole("ADMIN")) {
            return repository.findById(id).orElseThrow(() -> new TaskNotificationNotFoundException("id", id));
        } else {
            throw new AuthorizationDeniedException("Access denied");
        }
    }

    @Override
    public TaskNotification updateTaskNotification(Integer taskId, TaskNotification taskNotification) {
        if (SecurityUtils.currentUserHasAnyRole("ADMIN", "NOTIFIER")) {
            Optional<TaskNotification> optionalTaskNotification = repository.findByTaskId(taskId);
            if (optionalTaskNotification.isPresent()) {
                TaskNotification existingNotification = optionalTaskNotification.get();
                existingNotification.setLastNotifiedAt(taskNotification.getLastNotifiedAt());
                repository.save(existingNotification);
                return existingNotification;
            } else {
                taskNotification.setTask(Task.builder().id(taskId).build());
                repository.save(taskNotification);
                return taskNotification;
            }
        } else {
            throw new AuthorizationDeniedException("Access denied");
        }
    }

    @Override
    public TaskNotification getByTaskId(Integer taskId) {
        TaskNotification taskNotification = repository.findByTaskId(taskId).orElseThrow(() -> new TaskNotFoundException("id", taskId));
        if (SecurityUtils.currentUserHasAnyRole("ADMIN", "NOTIFIER") || taskNotification.getTask().getSchedule().getUserId().equals(SecurityUtils.currentUser().getId())) {
            return taskNotification;
        } else {
            throw new AuthorizationDeniedException("Access denied");
        }
    }

    @Override
    public TaskNotification create(TaskNotification entity) {
        if (SecurityUtils.currentUserHasRole("ADMIN") || entity.getTask().getSchedule().getUserId().equals(SecurityUtils.currentUser().getId())) {
            return repository.save(entity);
        } else {
            throw new AuthorizationDeniedException("Access denied");
        }
    }

    @Override
    public TaskNotification updateById(Integer id, TaskNotification entity) {
        getByTaskId(id);
        entity.setId(id);
        return repository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        getByTaskId(id);
        repository.deleteById(id);
    }

    @Override
    public List<Task> getTasksForNotification() {
        if (SecurityUtils.currentUserHasRole("NOTIFIER")) {
            return repository.findTasksForNotification(LocalDateTime.now());
        } else {
            throw new AuthorizationDeniedException("Access denied");
        }
    }
}
