package com.onetuks.ihub.dto.user;

import com.onetuks.ihub.entity.user.UserStatus;
import java.time.LocalDateTime;

public record UserResponse(
    String email,
    String name,
    String company,
    String position,
    String phoneNumber,
    String profileImageUrl,
    UserStatus status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
