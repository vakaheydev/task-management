package com.vaka.daily.domain.mapper;

import com.vaka.daily.domain.Task;
import com.vaka.daily.domain.dto.TaskDto;
import com.vaka.daily.service.domain.ScheduleService;
import com.vaka.daily.service.domain.TaskTypeService;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper implements DtoMapper<Task, TaskDto> {
    private final ScheduleService scheduleService;
    private final TaskTypeService taskTypeService;

    public TaskMapper(ScheduleService scheduleService, TaskTypeService taskTypeService) {
        this.scheduleService = scheduleService;
        this.taskTypeService = taskTypeService;
    }

    public Task fromDto(TaskDto dto) {
        return Task.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .deadline(dto.getDeadline())
                .status(dto.getStatus())
                .schedule(scheduleService.getById(dto.getScheduleId()))
                .taskType(taskTypeService.getById(dto.getTaskTypeId()))
                .build();
    }

    public TaskDto toDto(Task task) {
        return TaskDto.builder()
                .id(task.getId())
                .name(task.getName())
                .description(task.getDescription())
                .deadline(task.getDeadline())
                .status(task.getStatus())
                .scheduleId(task.getSchedule().getId())
                .taskTypeId(task.getTaskType().getId())
                .build();
    }
}
