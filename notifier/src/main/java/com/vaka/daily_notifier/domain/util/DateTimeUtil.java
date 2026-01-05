package com.vaka.daily_notifier.domain.util;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class DateTimeUtil {
    public static int getDaysFromNowTo(LocalDateTime dateTime) {
        long days = ChronoUnit.DAYS.between(LocalDate.now(), dateTime.toLocalDate());
        return Math.abs((int) days);
    }

    public static long getMinutesTo(LocalDateTime dateTime) {
        Duration between = Duration.between(LocalDateTime.now(), dateTime);
        return between.toMinutes();
    }
}
