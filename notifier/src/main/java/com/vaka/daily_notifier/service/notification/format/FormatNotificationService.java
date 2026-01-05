package com.vaka.daily_notifier.service.notification.format;

import com.vaka.daily_notifier.domain.Task;
import com.vaka.daily_notifier.domain.TaskType;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.vaka.daily_notifier.domain.util.TaskUtil.*;

@Service
public class FormatNotificationService {
    public String formatTaskForNotification(Task task, TaskType taskType) {
        String formattedTask = formatTask(task, taskType);
        String msg;

        if ("singular".equalsIgnoreCase(taskType.getName())) {
            msg = String.format("Напоминание\n\nУ Вас есть нерешённая задача: %s", formattedTask);
        } else if ("regular".equalsIgnoreCase(taskType.getName())) {
            msg = String.format("Не забудьте!\n\n%s", formattedTask);
        } else if ("repetitive".equalsIgnoreCase(taskType.getName())) {
            if (isTaskDeadLineLater(task)) {
                msg = String.format("Не забудьте!\n\n%s", formattedTask);
            } else {
                msg = String.format("Вы забыли выполнить регулярное задание\n\n%s", formattedTask);
            }
        } else {
            throw new IllegalArgumentException("Unknown task type: " + taskType);
        }

        return msg;
    }

    private String formatTask(Task task, TaskType taskType) {
        StringBuilder msg = new StringBuilder();
        msg.append(String.format("\nЗадание '%s' | %s | до %s | Тип: %s",
                task.getName(),
                task.getDescription(),
                formatDeadLine(task.getDeadline()),
                formatTaskType(taskType)));
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

