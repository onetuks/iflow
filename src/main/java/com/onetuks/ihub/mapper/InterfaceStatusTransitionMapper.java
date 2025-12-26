package com.onetuks.ihub.mapper;

import com.onetuks.ihub.dto.interfaces.InterfaceStatusTransitionCreateRequest;
import com.onetuks.ihub.dto.interfaces.InterfaceStatusTransitionResponse;
import com.onetuks.ihub.dto.interfaces.InterfaceStatusTransitionUpdateRequest;
import com.onetuks.ihub.entity.interfaces.InterfaceStatusTransition;
import java.time.LocalDateTime;

public final class InterfaceStatusTransitionMapper {

  private InterfaceStatusTransitionMapper() {
  }

  public static InterfaceStatusTransitionResponse toResponse(
      InterfaceStatusTransition transition) {
    return new InterfaceStatusTransitionResponse(
        transition.getTransitionId(),
        transition.getProject() != null ? transition.getProject().getProjectId() : null,
        transition.getFromStatus() != null ? transition.getFromStatus().getStatusId() : null,
        transition.getToStatus() != null ? transition.getToStatus().getStatusId() : null,
        transition.getAllowedRole(),
        transition.getStatus(),
        transition.getCreatedAt(),
        transition.getCreatedBy() != null ? transition.getCreatedBy().getEmail() : null);
  }

  public static void applyCreate(
      InterfaceStatusTransition transition, InterfaceStatusTransitionCreateRequest request) {
    transition.setTransitionId(UUIDProvider.provideUUID(InterfaceStatusTransition.TABLE_NAME));
    transition.setAllowedRole(request.allowedRole());
    transition.setStatus(request.status());
    transition.setCreatedAt(LocalDateTime.now());
  }

  public static void applyUpdate(
      InterfaceStatusTransition transition, InterfaceStatusTransitionUpdateRequest request) {
    if (request.allowedRole() != null) {
      transition.setAllowedRole(request.allowedRole());
    }
    if (request.status() != null) {
      transition.setStatus(request.status());
    }
  }
}
