package com.vaka.daily.domain.util;

import com.vaka.daily.domain.Task;

import java.time.LocalDateTime;
import java.time.Period;

public class TaskUtil {
    public static boolean isTaskSingular(Task task) {
        return isTaskType(task, "singular");
    }

    public static boolean isTaskRepetitive(Task task) {
        return isTaskType(task, "repetitive");
    }

    public static boolean isTaskRegular(Task task) {
        return isTaskType(task, "regular");
    }

    public static boolean isTaskType(Task task, String taskTypeName) {
        return task.getTaskType().getName().equals(taskTypeName);
    }

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
