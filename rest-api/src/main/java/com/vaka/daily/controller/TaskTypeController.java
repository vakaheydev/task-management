package com.vaka.daily.controller;

import com.vaka.daily.domain.TaskType;
import com.vaka.daily.exception.ValidationException;
import com.vaka.daily.service.domain.TaskTypeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/task_type")
@Slf4j
public class TaskTypeController {
    private final TaskTypeService taskTypeService;

    @Autowired
    public TaskTypeController(TaskTypeService taskTypeService) {
        this.taskTypeService = taskTypeService;
    }

    @PreAuthorize("hasAnyRole('USER', 'NOTIFIER', 'TELEGRAM')")
    @GetMapping
    public ResponseEntity<?> get() {
        return ResponseEntity.ok(taskTypeService.getAll());
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(taskTypeService.getById(id));
    }

    @PreAuthorize("hasRole('DEVELOPER')")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid TaskType taskType, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(taskTypeService.create(taskType));
    }

    @PreAuthorize("hasRole('DEVELOPER')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateById(@PathVariable("id") Integer id, @RequestBody @Valid TaskType taskType,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        return ResponseEntity.ok(taskTypeService.updateById(id, taskType));
    }

    @PreAuthorize("hasRole('DEVELOPER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Integer id) {
        taskTypeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
