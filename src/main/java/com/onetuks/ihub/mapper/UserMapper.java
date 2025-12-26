package com.onetuks.ihub.mapper;

import com.onetuks.ihub.dto.user.UserCreateRequest;
import com.onetuks.ihub.dto.user.UserResponse;
import com.onetuks.ihub.dto.user.UserUpdateRequest;
import com.onetuks.ihub.entity.user.User;
import java.time.LocalDateTime;

public final class UserMapper {

  private UserMapper() {
  }

  public static UserResponse toResponse(User user) {
    return new UserResponse(
        user.getEmail(),
        user.getName(),
        user.getCompany(),
        user.getPosition(),
        user.getPhoneNumber(),
        user.getProfileImageUrl(),
        user.getStatus(),
        user.getCreatedAt(),
        user.getUpdatedAt());
  }

  public static void applyCreate(User user, UserCreateRequest request) {
    LocalDateTime now = LocalDateTime.now();
    user.setEmail(request.email());
    user.setPassword(request.password());
    user.setName(request.name());
    user.setCompany(request.company());
    user.setPosition(request.position());
    user.setPhoneNumber(request.phoneNumber());
    user.setProfileImageUrl(request.profileImageUrl());
    user.setStatus(request.status());
    user.setCreatedAt(now);
    user.setUpdatedAt(now);
  }

  public static void applyUpdate(User user, UserUpdateRequest request) {
    if (request.email() != null) {
      user.setEmail(request.email());
    }
    if (request.password() != null) {
      user.setPassword(request.password());
    }
    if (request.name() != null) {
      user.setName(request.name());
    }
    if (request.company() != null) {
      user.setCompany(request.company());
    }
    if (request.position() != null) {
      user.setPosition(request.position());
    }
    if (request.phoneNumber() != null) {
      user.setPhoneNumber(request.phoneNumber());
    }
    if (request.profileImageUrl() != null) {
      user.setProfileImageUrl(request.profileImageUrl());
    }
    if (request.status() != null) {
      user.setStatus(request.status());
    }
    user.setUpdatedAt(LocalDateTime.now());
  }
}
