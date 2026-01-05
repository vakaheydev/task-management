package com.vaka.daily_notifier.scheduler;

import com.vaka.daily_notifier.service.notification.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@ConditionalOnProperty(value = "app.notification.enabled", havingValue = "true")
public class NotificationScheduler {
    private final NotificationService service;

    public NotificationScheduler(NotificationService service) {
        this.service = service;
    }

    @Scheduled(fixedDelay = 3, timeUnit = TimeUnit.SECONDS)
    public void notifyUsers() {
        log.info("Started notification scheduler");
        service.notifyUsers();
        log.info("Ended notification scheduler");
    }
}