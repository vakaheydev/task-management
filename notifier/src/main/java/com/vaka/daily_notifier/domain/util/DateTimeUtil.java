package com.vaka.daily_notifier.domain.util;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class DateTimeUtil {
    public static int getDaysFrom(LocalDateTime dateTime) {
        long days = ChronoUnit.DAYS.between(LocalDate.now(), dateTime.toLocalDate());
        return (int) days;
    }

    public static int getDaysTo(LocalDateTime dateTime) {
        long days = ChronoUnit.DAYS.between(LocalDate.now(), dateTime.toLocalDate());
        return (int) days;
    }

    public static long getMinutesTo(LocalDateTime dateTime) {
        Duration between = Duration.between(LocalDateTime.now(), dateTime);
        return between.toMinutes();
    }
}
