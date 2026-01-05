package com.vaka.daily.domain.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;

public class DateTimeUtil {
    public static int getDaysFrom(LocalDateTime dateTime) {
        Period between = Period.between(LocalDateTime.now().toLocalDate(), dateTime.toLocalDate());
        return between.getDays();
    }

    public static int getDaysTo(LocalDateTime dateTime) {
        Period between = Period.between(LocalDateTime.now().toLocalDate(), dateTime.toLocalDate());
        return between.getDays();
    }

    public static long getMinutesTo(LocalDateTime dateTime) {
        Duration between = Duration.between(LocalDateTime.now(), dateTime);
        return between.toMinutes();
    }
}
