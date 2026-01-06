package com.vaka.daily.controller;

import com.vaka.daily.domain.Schedule;
import com.vaka.daily.domain.Task;
import com.vaka.daily.domain.User;
import com.vaka.daily.service.domain.MeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/me")
@Slf4j
public class MeController {
    private final MeService meService;

    public MeController(MeService meService) {
        this.meService = meService;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ResponseEntity<User> getMe() {
        return ResponseEntity.ok(meService.getMe());
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/schedules")
    public ResponseEntity<List<Schedule>> getMySchedules() {
        return ResponseEntity.ok(meService.getMySchedules());
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getMyTasks() {
        return ResponseEntity.ok(meService.getMyTasks());
    }
}
