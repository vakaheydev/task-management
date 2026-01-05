package com.vaka.daily.domain.mapper;

import com.vaka.daily.domain.Schedule;
import com.vaka.daily.domain.dto.ScheduleDto;
import com.vaka.daily.exception.notfound.UserNotFoundException;
import com.vaka.daily.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class ScheduleMapper implements DtoMapper<Schedule, ScheduleDto> {
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    public ScheduleMapper(UserRepository userRepository, TaskMapper taskMapper) {
        this.userRepository = userRepository;
        this.taskMapper = taskMapper;
    }

    @Override
    public Schedule fromDto(ScheduleDto dto) {
        if (dto.getTasks() == null) {
            dto.setTasks(new ArrayList<>());
        }

        return Schedule.builder()
                .id(dto.getId())
                .name(dto.getName())
                .user(userRepository.findById(dto.getUserId())
                        .orElseThrow(() -> new UserNotFoundException("id", dto.getUserId())))
                .tasks(dto.getTasks().stream().map(taskMapper::fromDto).toList())
                .build();
    }

    @Override
    public ScheduleDto toDto(Schedule schedule) {
        return ScheduleDto.builder()
                .id(schedule.getId())
                .name(schedule.getName())
                .userId(schedule.getUser().getId())
                .tasks(schedule.getTasks().stream().map(taskMapper::toDto).toList())
                .build();
    }
}
