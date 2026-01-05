package com.vaka.daily_notifier.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskNotification {
    private Integer id;
    private LocalDateTime lastNotifiedAt;
}
