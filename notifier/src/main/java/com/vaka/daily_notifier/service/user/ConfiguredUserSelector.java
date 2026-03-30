package com.vaka.daily_notifier.service.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ConfiguredUserSelector implements UserSelector {

    private final List<Integer> userIds;

    public ConfiguredUserSelector(@Value("${app.daily-summary.user-ids}") String userIdsRaw) {
        this.userIds = Arrays.stream(userIdsRaw.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .toList();
    }

    @Override
    public List<Integer> selectUserIds() {
        return userIds;
    }
}
