package com.vaka.daily.service.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class NotificationSchedulerService {
    private final NotificationService notificationService;

    public NotificationSchedulerService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Transactional
    public void process() {
        notificationService.notifyUsers();
    }
}
