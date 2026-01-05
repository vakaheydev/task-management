package com.vaka.daily.service.domain;

import com.vaka.daily.domain.TaskType;
import com.vaka.daily.exception.notfound.TaskTypeNotFoundException;
import com.vaka.daily.repository.TaskTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskTypeServiceImpl implements TaskTypeService {
    private TaskTypeRepository taskTypeRepository;

    public TaskTypeServiceImpl(TaskTypeRepository taskTypeRepository) {
        this.taskTypeRepository = taskTypeRepository;
    }

    @Override
    public List<TaskType> getAll() {
        return taskTypeRepository.findAll();
    }

    @Override
    public TaskType getById(Integer id) {
        var optional = taskTypeRepository.findById(id);
        return optional.orElseThrow(() -> new TaskTypeNotFoundException("id", id));
    }

    @Override
    public TaskType create(TaskType entity) {
        return taskTypeRepository.save(entity);
    }

    @Override
    public TaskType updateById(Integer id, TaskType entity) {
        if (!taskTypeRepository.existsById(id)) {
            throw new TaskTypeNotFoundException("id", id);
        }

        entity.setId(id);
        return taskTypeRepository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        if (!taskTypeRepository.existsById(id)) {
            throw new TaskTypeNotFoundException("id", id);
        }

        taskTypeRepository.deleteById(id);
    }
}
