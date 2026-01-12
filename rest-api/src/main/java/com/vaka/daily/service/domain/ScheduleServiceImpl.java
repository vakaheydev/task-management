package com.vaka.daily.service.domain;

import com.vaka.daily.domain.Schedule;
import com.vaka.daily.domain.Task;
import com.vaka.daily.domain.User;
import com.vaka.daily.exception.notfound.ScheduleNotFoundException;
import com.vaka.daily.exception.notfound.UserNotFoundException;
import com.vaka.daily.repository.ScheduleRepository;
import com.vaka.daily.repository.TaskTypeRepository;
import com.vaka.daily.repository.UserRepository;
import com.vaka.daily.security.SecurityUtils;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final TaskTypeRepository taskTypeRepository;

    @Autowired
    public ScheduleServiceImpl(ScheduleRepository scheduleRepository, UserRepository userRepository,
                               TaskTypeRepository taskTypeRepository) {
        this.scheduleRepository = scheduleRepository;
        this.userRepository = userRepository;
        this.taskTypeRepository = taskTypeRepository;
    }

    @Override
    public List<Schedule> getAll() {
        if (SecurityUtils.currentUserHasAnyRole("ADMIN", "NOTIFIER")) {
            return scheduleRepository.findAll();
        } else {
            throw new AuthorizationDeniedException("Access denied");
        }
    }

    @Override
    public Schedule getById(Integer id) {
        Schedule schedule = scheduleRepository.findById(id).orElseThrow(() -> new ScheduleNotFoundException("id", id));
        if (SecurityUtils.currentUserHasAnyRole("ADMIN", "NOTIFIER") || schedule.getUserId().equals(SecurityUtils.currentUser().getId())) {
            return schedule;
        } else {
            throw new AuthorizationDeniedException("Access denied");
        }
    }

    @Override
    public List<Schedule> getByName(String name) {
        if (SecurityUtils.currentUserHasRole("ADMIN")) {
            return scheduleRepository.findByName(name);
        } else {
            throw new AuthorizationDeniedException("Access denied");
        }
    }

    @Override
    public Schedule createDefaultSchedule(User user) {
        Schedule schedule = new Schedule("main", user);
        schedule.setTasks(new ArrayList<>());
        schedule.addTask(createDefaultTask(schedule));

        return create(schedule);
    }

    @Override
    public List<Schedule> getByUserId(Integer id) {
        if (SecurityUtils.currentUserHasAnyRole("ADMIN", "TELEGRAM") || id.equals(SecurityUtils.currentUser().getId())) {
            if (!userRepository.existsById(id)) {
                throw new UserNotFoundException("id", id);
            }

            return scheduleRepository.findByUserId(id);
        } else {
            throw new AuthorizationDeniedException("Access denied");
        }
    }

    @Override
    public Schedule create(Schedule entity) {
        if (SecurityUtils.currentUserHasRole("ADMIN") || entity.getUser().getId().equals(SecurityUtils.currentUser().getId())) {
            if (entity.getUser() == null) {
                throw new ValidationException("Schedule with null user");
            }

            if (entity.getTasks() == null) {
                entity.setTasks(new ArrayList<>());
            }

            return scheduleRepository.save(entity);
        } else {
            throw new AuthorizationDeniedException("Access denied");
        }
    }

    @Override
    public Schedule updateById(Integer id, Schedule entity) {
        getById(id); // Check authorization and existence
        entity.setId(id);
        return scheduleRepository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        getById(id); // Check authorization and existence
        scheduleRepository.deleteById(id);
    }

    private Task createDefaultTask(Schedule schedule) {
        return Task.builder()
                .name("First task")
                .description("Let's do something great!")
                .deadline(LocalDateTime.now().plusDays(7))
                .schedule(schedule)
                .taskType(taskTypeRepository.findByName("singular").orElseThrow())
                .status(false)
                .build();
    }
}
