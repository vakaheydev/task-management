package com.vaka.daily.mcp;

import com.vaka.daily.domain.Schedule;
import com.vaka.daily.domain.Task;
import com.vaka.daily.domain.dto.ScheduleDto;
import com.vaka.daily.domain.dto.TaskDto;
import com.vaka.daily.domain.mapper.ScheduleMapper;
import com.vaka.daily.domain.mapper.TaskMapper;
import com.vaka.daily.service.domain.MeService;
import com.vaka.daily.service.domain.ScheduleService;
import com.vaka.daily.service.domain.TaskService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class McpTaskTools {

    private final TaskService taskService;
    private final MeService meService;
    private final TaskMapper taskMapper;
    private final ScheduleService scheduleService;
    private final ScheduleMapper scheduleMapper;

    public McpTaskTools(TaskService taskService, MeService meService, TaskMapper taskMapper,
                        ScheduleService scheduleService, ScheduleMapper scheduleMapper) {
        this.taskService = taskService;
        this.meService = meService;
        this.taskMapper = taskMapper;
        this.scheduleService = scheduleService;
        this.scheduleMapper = scheduleMapper;
    }

    @Tool(description = "Get all tasks of the current authenticated user")
    public List<TaskDto> getMyTasks() {
        return meService.getMyTasks().stream()
                .map(taskMapper::toDto)
                .toList();
    }

    @Tool(description = "Get a task by its ID")
    public TaskDto getTaskById(@ToolParam(description = "Task ID") Integer id) {
        return taskMapper.toDto(taskService.getById(id));
    }

    @Tool(description = "Create a new task for the current user")
    public TaskDto createTask(
            @ToolParam(description = "Task name (max 100 chars)") String name,
            @ToolParam(description = "Task description (max 100 chars)") String description,
            @ToolParam(description = "Deadline in ISO format, e.g. 2024-12-31T23:59:59") String deadline,
            @ToolParam(description = "Schedule ID") Integer scheduleId,
            @ToolParam(description = "Task type ID") Integer taskTypeId) {
        TaskDto dto = TaskDto.builder()
                .name(name)
                .description(description)
                .deadline(LocalDateTime.parse(deadline))
                .isCompleted(false)
                .scheduleId(scheduleId)
                .taskTypeId(taskTypeId)
                .build();
        Task created = taskService.create(taskMapper.fromDto(dto));
        return taskMapper.toDto(created);
    }

    @Tool(description = "Mark a task as completed (status = true)")
    public TaskDto completeTask(@ToolParam(description = "Task ID") Integer id) {
        Task task = taskService.getById(id);
        task.setIsCompleted(true);
        return taskMapper.toDto(taskService.updateById(id, task));
    }

    @Tool(description = "Update an existing task")
    public TaskDto updateTask(
            @ToolParam(description = "Task ID") Integer id,
            @ToolParam(description = "New task name") String name,
            @ToolParam(description = "New task description") String description,
            @ToolParam(description = "New deadline in ISO format") String deadline,
            @ToolParam(description = "Task status: true = done, false = not done") Boolean status,
            @ToolParam(description = "Schedule ID") Integer scheduleId,
            @ToolParam(description = "Task type ID") Integer taskTypeId) {
        TaskDto dto = TaskDto.builder()
                .name(name)
                .description(description)
                .deadline(LocalDateTime.parse(deadline))
                .isCompleted(status)
                .scheduleId(scheduleId)
                .taskTypeId(taskTypeId)
                .build();
        return taskMapper.toDto(taskService.updateById(id, taskMapper.fromDto(dto)));
    }

    @Tool(description = "Delete a task by ID")
    public String deleteTask(@ToolParam(description = "Task ID") Integer id) {
        taskService.deleteById(id);
        return "Task with id=" + id + " deleted successfully";
    }

    @Tool(description = "Search current user's tasks by name (case-insensitive)")
    public List<TaskDto> searchTasksByName(
            @ToolParam(description = "Search string for task name") String name) {
        return taskService.searchByName(name).stream()
                .map(taskMapper::toDto)
                .toList();
    }

    @Tool(description = "Get all schedules of the current authenticated user")
    public List<ScheduleDto> getMySchedules() {
        return meService.getMySchedules().stream()
                .map(scheduleMapper::toDto)
                .toList();
    }

    @Tool(description = "Create a new schedule for the current user")
    public ScheduleDto createSchedule(
            @ToolParam(description = "Schedule name (max 100 chars)") String name) {
        Schedule schedule = new Schedule(name, meService.getMe());
        return scheduleMapper.toDto(scheduleService.create(schedule));
    }

    @Tool(description = "Move a task to a different schedule")
    public TaskDto moveTaskToSchedule(
            @ToolParam(description = "Task ID") Integer taskId,
            @ToolParam(description = "Target schedule ID") Integer scheduleId) {
        Task task = taskService.getById(taskId);
        task.setSchedule(scheduleService.getById(scheduleId));
        return taskMapper.toDto(taskService.updateById(taskId, task));
    }
}
