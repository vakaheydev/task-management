package com.vaka.daily.service.domain;

import com.vaka.daily.domain.Task;
import com.vaka.daily.domain.TaskNotification;
import com.vaka.daily.exception.notfound.TaskNotFoundException;
import com.vaka.daily.exception.notfound.TaskNotificationNotFoundException;
import com.vaka.daily.repository.TaskNotificationRepository;
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
        return repository.findAll();
    }

    @Override
    public TaskNotification getById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new TaskNotificationNotFoundException("id", id));
    }

    @Override
    public TaskNotification updateTaskNotification(Integer taskId, TaskNotification taskNotification) {
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
    }

    @Override
    public TaskNotification getByTaskId(Integer taskId) {
        return repository.findByTaskId(taskId).orElseThrow(() -> new TaskNotFoundException("id", taskId));
    }

    @Override
    public TaskNotification create(TaskNotification entity) {
        return repository.save(entity);
    }

    @Override
    public TaskNotification updateById(Integer id, TaskNotification entity) {
        if (!repository.existsById(id)) {
            throw new TaskNotificationNotFoundException("id", id);
        }

        entity.setId(id);
        return repository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        if (!repository.existsById(id)) {
            throw new TaskNotificationNotFoundException("id", id);
        }

        repository.deleteById(id);
    }

    @Override
    public List<Task> getTasksForNotification() {
        return repository.findTasksForNotification(LocalDateTime.now());
    }
}
