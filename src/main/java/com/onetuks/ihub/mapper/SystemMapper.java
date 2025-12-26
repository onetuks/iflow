package com.onetuks.ihub.mapper;

import com.onetuks.ihub.dto.system.SystemCreateRequest;
import com.onetuks.ihub.dto.system.SystemResponse;
import com.onetuks.ihub.dto.system.SystemUpdateRequest;
import com.onetuks.ihub.entity.system.System;
import java.time.LocalDateTime;

public final class SystemMapper {

  private SystemMapper() {
  }

  public static SystemResponse toResponse(System system) {
    return new SystemResponse(
        system.getSystemId(),
        system.getProject() != null ? system.getProject().getProjectId() : null,
        system.getSystemCode(),
        system.getStatus(),
        system.getDescription(),
        system.getSystemType(),
        system.getEnvironment(),
        system.getCreatedBy() != null ? system.getCreatedBy().getEmail() : null,
        system.getUpdatedBy() != null ? system.getUpdatedBy().getEmail() : null,
        system.getCreatedAt(),
        system.getUpdatedAt());
  }

  public static void applyCreate(System system, SystemCreateRequest request) {
    LocalDateTime now = LocalDateTime.now();
    system.setSystemId(UUIDProvider.provideUUID(System.TABLE_NAME));
    system.setSystemCode(request.systemCode());
    system.setStatus(request.status());
    system.setDescription(request.description());
    system.setSystemType(request.systemType());
    system.setEnvironment(request.environment());
    system.setCreatedAt(now);
    system.setUpdatedAt(now);
  }

  public static void applyUpdate(System system, SystemUpdateRequest request) {
    if (request.systemCode() != null) {
      system.setSystemCode(request.systemCode());
    }
    if (request.status() != null) {
      system.setStatus(request.status());
    }
    if (request.description() != null) {
      system.setDescription(request.description());
    }
    if (request.systemType() != null) {
      system.setSystemType(request.systemType());
    }
    if (request.environment() != null) {
      system.setEnvironment(request.environment());
    }
    system.setUpdatedAt(LocalDateTime.now());
  }
}
