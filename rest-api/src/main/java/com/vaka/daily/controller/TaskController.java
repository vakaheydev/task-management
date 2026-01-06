package com.vaka.daily.controller;

import com.vaka.daily.domain.Task;
import com.vaka.daily.domain.TaskNotification;
import com.vaka.daily.domain.dto.TaskDto;
import com.vaka.daily.domain.mapper.TaskMapper;
import com.vaka.daily.exception.ValidationException;
import com.vaka.daily.service.domain.ScheduleService;
import com.vaka.daily.service.domain.TaskNotificationService;
import com.vaka.daily.service.domain.TaskService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/task")
@Slf4j
public class TaskController {
    private final TaskService taskService;
    private final TaskNotificationService taskNotificationService;
    private final ScheduleService scheduleService;
    private final TaskMapper taskMapper;

    public TaskController(TaskService taskService, TaskNotificationService taskNotificationService, ScheduleService scheduleService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskNotificationService = taskNotificationService;
        this.scheduleService = scheduleService;
        this.taskMapper = taskMapper;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'NOTIFIER')")
    @GetMapping
    public ResponseEntity<?> get() {
        return ResponseEntity.ok(taskService.getAll().stream().map(taskMapper::toDto));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(taskMapper.toDto(taskService.getById(id)));
    }

    @PreAuthorize("hasAnyRole('USER', 'NOTIFIER')")
    @GetMapping("/{id}/notification")
    public ResponseEntity<?> getByIdNotification(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(taskNotificationService.getByTaskId(id));
    }

    @PreAuthorize("hasAnyRole('USER', 'NOTIFIER')")
    @PutMapping("/{id}/notification")
    public ResponseEntity<?> updateNotification(@PathVariable("id") Integer id,
                                                @RequestBody @Valid TaskNotification taskNotification,
                                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        TaskNotification updated = taskNotificationService.updateTaskNotification(id, taskNotification);
        return ResponseEntity.ok(updated);
    }


//    @GetMapping("/search")
//    public ResponseEntity<?> search(@RequestParam(name = "name", required = false) String name,
//                                    @RequestParam(name = "user_id", required = false) Integer userId) {
//        if(name != null && userId != null) {
//            // TODO: 6/19/2024 Implements criteria and specifications
//        }
//        else if(name != null) {
//            return ResponseEntity.ok(service.getByName(name));
//        }
//
//        return ResponseEntity.ok(service.getByUserId(userId));
//    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid TaskDto taskDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        Task task = taskMapper.fromDto(taskDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.create(task));
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateById(@PathVariable("id") Integer id, @RequestBody @Valid TaskDto taskDto,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        Task updated = taskService.updateById(id, taskMapper.fromDto(taskDto));

        return ResponseEntity.ok(taskMapper.toDto(updated));
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Integer id) {
        taskService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
