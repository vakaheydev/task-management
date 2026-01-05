package com.vaka.daily_notifier.domain.util;

import com.vaka.daily_notifier.domain.Task;

import java.time.LocalDateTime;
import java.time.Period;

public class TaskUtil {
    public static boolean isTaskDeadLineLater(Task task) {
        return task.getDeadline().isAfter(LocalDateTime.now());
    }

    public static boolean isTaskDeadLineEarlier(Task task) {
        return task.getDeadline().isBefore(LocalDateTime.now());
    }

    public static int getDaysToDeadLine(Task task) {
        Period between = Period.between(LocalDateTime.now().toLocalDate(), task.getDeadline().toLocalDate());
        return between.getDays();
    }
}
