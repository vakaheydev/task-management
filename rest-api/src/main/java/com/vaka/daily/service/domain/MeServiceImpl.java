package com.vaka.daily.service.domain;

import com.vaka.daily.domain.Schedule;
import com.vaka.daily.domain.Task;
import com.vaka.daily.domain.User;
import com.vaka.daily.security.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MeServiceImpl implements MeService {
    private final UserService userService;
    private final ScheduleService scheduleService;
    private final TaskService taskService;

    public MeServiceImpl(UserService userService, ScheduleService scheduleService, TaskService taskService) {
        this.userService = userService;
        this.scheduleService = scheduleService;
        this.taskService = taskService;
    }

    @Override
    public User getMe() {
        int currentUserId = SecurityUtils.currentUser().getId();
        return userService.getById(currentUserId);
    }

    @Override
    public List<Schedule> getMySchedules() {
        return getMe().getSchedules();
    }

    @Override
    public List<Task> getMyTasks() {
        return getMe().getSchedules().stream()
                .flatMap(schedule -> schedule.getTasks().stream())
                .toList();
    }
}
