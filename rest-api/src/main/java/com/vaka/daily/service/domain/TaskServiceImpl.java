package com.vaka.daily.service.domain;

import com.vaka.daily.domain.Task;
import com.vaka.daily.exception.notfound.TaskNotFoundException;
import com.vaka.daily.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final ScheduleService scheduleService;
    private final TaskTypeService taskTypeService;

    public TaskServiceImpl(TaskRepository taskRepository, ScheduleService scheduleService,
                           TaskTypeService taskTypeService) {
        this.taskRepository = taskRepository;
        this.scheduleService = scheduleService;
        this.taskTypeService = taskTypeService;
    }

    @Override
    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    @Override
    public Task getById(Integer id) {
        var optional = taskRepository.findById(id);
        return optional.orElseThrow(() -> new TaskNotFoundException("id", id));
    }

    @Override
    public Task create(Task entity) {
        return taskRepository.save(entity);
    }

    @Override
    public Task updateById(Integer id, Task entity) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException("id", id);
        }

        entity.setId(id);
        return taskRepository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException("id", id);
        }

        taskRepository.deleteById(id);
    }
}
