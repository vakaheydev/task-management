package com.vaka.daily_notifier.scheduler;

import com.vaka.daily_notifier.service.summary.DailySummaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(value = "app.daily-summary.enabled", havingValue = "true")
public class DailySummaryScheduler {

    private final DailySummaryService dailySummaryService;

    public DailySummaryScheduler(DailySummaryService dailySummaryService) {
        this.dailySummaryService = dailySummaryService;
    }

    @Scheduled(cron = "0 0 9 * * ?", zone = "Europe/Moscow")
    public void morning() {
        log.info("Started morning summary scheduler");
        dailySummaryService.sendMorningSummaries();
        log.info("Ended morning summary scheduler");
    }

    @Scheduled(cron = "0 0 20 * * ?", zone = "Europe/Moscow")
    public void evening() {
        log.info("Started evening summary scheduler");
        dailySummaryService.sendEveningSummaries();
        log.info("Ended evening summary scheduler");
    }
}
