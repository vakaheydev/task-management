package com.vaka.daily.service.domain;

import com.vaka.daily.domain.Schedule;
import com.vaka.daily.domain.Task;
import com.vaka.daily.domain.User;

import java.util.List;

public interface MeService {
    User getMe();
    List<Schedule> getMySchedules();
    List<Task> getMyTasks();
}
