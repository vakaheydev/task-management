package com.vaka.daily.controller;

import com.vaka.daily.domain.User;
import com.vaka.daily.domain.Task;
import com.vaka.daily.domain.Schedule;
import com.vaka.daily.domain.dto.UserDto;
import com.vaka.daily.exception.ValidationException;
import com.vaka.daily.service.domain.UserService;
import com.vaka.daily.service.domain.ScheduleService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {
    private final UserService service;
    private final ScheduleService scheduleService;

    @Autowired
    public UserController(UserService service, ScheduleService scheduleService) {
        this.service = service;
        this.scheduleService = scheduleService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<?> get() {
        return ResponseEntity.ok(service.getAll());
    }

    @PreAuthorize("hasAnyRole('USER', 'NOTIFIER', 'TELEGRAM')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'TELEGRAM')")
    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam(name = "user_type_name", required = false) String userTypeName,
            @RequestParam(name = "login", required = false) String login,
            @RequestParam(name = "tgId", required = false) Long tgId) {
        if (userTypeName != null) {
            return ResponseEntity.ok(service.getByUserTypeName(userTypeName));
        } else if (login != null) {
            return ResponseEntity.ok(service.getByUniqueName(login));
        } else if (tgId != null) {
            return ResponseEntity.ok(service.getByTgId(tgId));
        }

        throw new IllegalArgumentException("No specified criteria");
        // TODO: 6/19/2024 Implements criteria and specifications
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(user));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/dto")
    public ResponseEntity<?> createFromDTO(@RequestBody @Valid UserDto userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(service.createFromDTO(userDTO));
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateById(@PathVariable("id") Integer id, @RequestBody @Valid User user,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        return ResponseEntity.ok(service.updateById(id, user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Integer id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('USER', 'TELEGRAM')")
    @GetMapping("/{id}/tasks")
    public ResponseEntity<?> getTasksByUserId(@PathVariable("id") Integer id) {
        List<Schedule> schedules = scheduleService.getByUserId(id);
        List<Task> tasks = schedules.stream()
                .flatMap(s -> s.getTasks().stream())
                .collect(Collectors.toList());
        return ResponseEntity.ok(tasks);
    }

    @PreAuthorize("hasAnyRole('USER', 'TELEGRAM')")
    @GetMapping("/{id}/schedules")
    public ResponseEntity<?> getSchedulesByUserId(@PathVariable("id") Integer id) {
        List<Schedule> schedules = scheduleService.getByUserId(id);
        return ResponseEntity.ok(schedules);
    }
}
