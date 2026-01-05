package com.vaka.daily.service.domain;

import com.vaka.daily.domain.Schedule;
import com.vaka.daily.domain.Task;
import com.vaka.daily.domain.User;
import com.vaka.daily.exception.notfound.ScheduleNotFoundException;
import com.vaka.daily.exception.notfound.UserNotFoundException;
import com.vaka.daily.repository.ScheduleRepository;
import com.vaka.daily.repository.TaskTypeRepository;
import com.vaka.daily.repository.UserRepository;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
        return scheduleRepository.findAll();
    }

    @Override
    public Schedule getById(Integer id) {
        var temp = scheduleRepository.findById(id);
        return temp.orElseThrow(() -> new ScheduleNotFoundException("id", id));
    }

    @Override
    public List<Schedule> getByName(String name) {
        return scheduleRepository.findByName(name);
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
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("id", id);
        }

        return scheduleRepository.findByUserId(id);
    }

    @Override
    public Schedule create(Schedule entity) {
        if (entity.getUser() == null) {
            throw new ValidationException("Schedule with null user");
        }

        if (entity.getTasks() == null) {
            entity.setTasks(new ArrayList<>());
        }

        return scheduleRepository.save(entity);
    }

    @Override
    public Schedule updateById(Integer id, Schedule entity) {
        if (!scheduleRepository.existsById(id)) {
            throw new ScheduleNotFoundException("id", id);
        }

        entity.setId(id);
        return scheduleRepository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        if (!scheduleRepository.existsById(id)) {
            throw new ScheduleNotFoundException("id", id);
        }

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
