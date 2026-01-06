package com.vaka.daily_notifier.domain;

import java.time.Instant;

public record AuthResponse(
        String accessToken,
        Instant expiresAt
) {}