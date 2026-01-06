package com.vaka.daily.domain.dto;

import java.time.Instant;

public record JwtResponse(
        String accessToken,
        Instant expiresAt
) {}
