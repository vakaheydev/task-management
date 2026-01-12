package com.vaka.daily.service.domain;

import com.vaka.daily.domain.Task;
import com.vaka.daily.exception.notfound.TaskNotFoundException;
import com.vaka.daily.repository.TaskRepository;
import com.vaka.daily.security.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public List<Task> getAll() {
        if (SecurityUtils.currentUserHasAnyRole("ADMIN", "NOTIFIER", "TELEGRAM")) {
            return taskRepository.findAll();
        } else {
            throw new AuthorizationDeniedException("Access denied");
        }
    }

    @Override
    public Task getById(Integer id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("id", id));
        if (SecurityUtils.currentUserHasAnyRole("ADMIN", "TELEGRAM") || task.getSchedule().getUserId().equals(SecurityUtils.currentUser().getId())) {
            return task;
        } else {
            throw new AuthorizationDeniedException("Access denied");
        }
    }

    @Override
    public Task create(Task entity) {
        if (SecurityUtils.currentUserHasRole("ADMIN") || entity.getSchedule().getUserId().equals(SecurityUtils.currentUser().getId())) {
            return taskRepository.save(entity);
        } else {
            throw new AuthorizationDeniedException("Access denied");
        }
    }

    @Override
    public Task updateById(Integer id, Task entity) {
        if (SecurityUtils.currentUserHasAnyRole("ADMIN") || entity.getSchedule().getUserId().equals(SecurityUtils.currentUser().getId())) {
            if (!taskRepository.existsById(id)) {
                throw new TaskNotFoundException("id", id);
            }

            entity.setId(id);
            return taskRepository.save(entity);
        } else {
            throw new AuthorizationDeniedException("Access denied");
        }
    }

    @Override
    public void deleteById(Integer id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("id", id));
        if (SecurityUtils.currentUserHasRole("ADMIN") || task.getSchedule().getUserId().equals(SecurityUtils.currentUser().getId())) {
            taskRepository.deleteById(id);
        } else {
            throw new AuthorizationDeniedException("Access denied");
        }
    }
}
