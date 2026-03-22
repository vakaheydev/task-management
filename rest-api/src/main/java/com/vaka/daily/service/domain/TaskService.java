package com.vaka.daily.service.domain;

import com.vaka.daily.domain.Task;
import com.vaka.daily.service.abstraction.CommonService;

import java.util.List;

public interface TaskService extends CommonService<Task> {
    List<Task> searchByName(String name);
}
