package com.vaka.daily_notifier.controller;

import com.vaka.daily_notifier.service.summary.DailySummaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/summary")
@Slf4j
public class SummaryController {

    private final DailySummaryService dailySummaryService;

    public SummaryController(DailySummaryService dailySummaryService) {
        this.dailySummaryService = dailySummaryService;
    }

    @PostMapping("/morning")
    public ResponseEntity<Map<String, String>> triggerMorning() {
        log.info("Manual trigger: morning summary");
        CompletableFuture.runAsync(dailySummaryService::sendMorningSummaries);
        return ResponseEntity.accepted().body(Map.of("status", "Morning summary started"));
    }

    @PostMapping("/evening")
    public ResponseEntity<Map<String, String>> triggerEvening() {
        log.info("Manual trigger: evening summary");
        CompletableFuture.runAsync(dailySummaryService::sendEveningSummaries);
        return ResponseEntity.accepted().body(Map.of("status", "Evening summary started"));
    }
}
