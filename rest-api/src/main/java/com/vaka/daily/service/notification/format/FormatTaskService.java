package com.vaka.daily.service.notification.format;

import com.vaka.daily.domain.Task;
import com.vaka.daily.domain.TaskType;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.vaka.daily.domain.util.TaskUtil.*;

@Service
public class FormatTaskService {
    public String formatTaskForNotification(Task task) {
        String formattedTask = formatTask(task);
        String msg;

        if (isTaskSingular(task)) {
            msg = String.format("Напоминание\n\nУ Вас есть нерешённая задача: %s", formattedTask);
        } else if (isTaskRegular(task)) {
            msg = String.format("Не забудьте!\n\n%s", formattedTask);
        } else if (isTaskRepetitive(task)) {
            if (isTaskDeadLineLater(task)) {
                msg = String.format("Не забудьте!\n\n%s", formattedTask);
            } else {
                msg = String.format("Вы забыли выполнить регулярное задание\n\n%s", formattedTask);
            }
        } else {
            throw new IllegalArgumentException("Incorrect task type: " + task.getTaskType());
        }


        return msg;
    }

    private String formatTask(Task task) {
        StringBuilder msg = new StringBuilder();
        msg.append(String.format("\nЗадание '%s' | %s | до %s | Тип: %s",
                task.getName(),
                task.getDescription(),
                formatDeadLine(task.getDeadline()),
                formatTaskType(task.getTaskType())));
        msg.append("\n---\n");
        return msg.toString();
    }

    private String formatTaskType(TaskType taskType) {
        return switch (taskType.getName()) {
            case "singular" -> "Одиночное";
            case "repetitive" -> "Повторяемое";
            case "regular" -> "Регулярное";
            default -> "Неизвестный тип";
        };
    }

    private String formatDeadLine(LocalDateTime deadLine) {
        return deadLine.format(DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm", Locale.forLanguageTag("ru")));
    }
}

