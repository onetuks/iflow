package com.onetuks.ihub.dto.auth;

import java.time.Instant;

public record AuthResponse(
    String token,
    String tokenType,
    Instant expiresAt
) {
}
