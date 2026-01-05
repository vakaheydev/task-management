package com.vaka.daily.service.domain;

import com.vaka.daily.domain.Schedule;
import com.vaka.daily.domain.User;
import com.vaka.daily.service.abstraction.CommonService;

import java.util.List;

public interface ScheduleService extends CommonService<Schedule> {
    List<Schedule> getByUserId(Integer id);

    List<Schedule> getByName(String name);

    Schedule createDefaultSchedule(User user);
}
