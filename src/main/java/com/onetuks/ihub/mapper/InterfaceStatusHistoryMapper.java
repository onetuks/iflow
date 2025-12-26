package com.onetuks.ihub.mapper;

import com.onetuks.ihub.dto.interfaces.InterfaceStatusHistoryCreateRequest;
import com.onetuks.ihub.dto.interfaces.InterfaceStatusHistoryResponse;
import com.onetuks.ihub.dto.interfaces.InterfaceStatusHistoryUpdateRequest;
import com.onetuks.ihub.entity.interfaces.InterfaceStatusHistory;
import java.time.LocalDateTime;

public final class InterfaceStatusHistoryMapper {

  private InterfaceStatusHistoryMapper() {
  }

  public static InterfaceStatusHistoryResponse toResponse(InterfaceStatusHistory history) {
    return new InterfaceStatusHistoryResponse(
        history.getHistoryId(),
        history.getAnInterface() != null ? history.getAnInterface().getInterfaceId() : null,
        history.getFromStatus() != null ? history.getFromStatus().getStatusId() : null,
        history.getToStatus() != null ? history.getToStatus().getStatusId() : null,
        history.getChangedBy() != null ? history.getChangedBy().getEmail() : null,
        history.getChangedAt(),
        history.getRelatedTask() != null ? history.getRelatedTask().getTaskId() : null,
        history.getReason());
  }

  public static void applyCreate(InterfaceStatusHistory history,
      InterfaceStatusHistoryCreateRequest request) {
    history.setHistoryId(UUIDProvider.provideUUID(InterfaceStatusHistory.TABLE_NAME));
    history.setChangedAt(LocalDateTime.now());
    history.setReason(request.reason());
  }

  public static void applyUpdate(InterfaceStatusHistory history,
      InterfaceStatusHistoryUpdateRequest request) {
    if (request.toStatusId() != null) {
      // handled in service
    }
    if (request.reason() != null) {
      history.setReason(request.reason());
    }
    history.setChangedAt(LocalDateTime.now());
  }
}
