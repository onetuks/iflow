package com.onetuks.ihub.mapper;

import com.onetuks.ihub.dto.interfaces.InterfaceRevisionCreateRequest;
import com.onetuks.ihub.dto.interfaces.InterfaceRevisionResponse;
import com.onetuks.ihub.dto.interfaces.InterfaceRevisionUpdateRequest;
import com.onetuks.ihub.entity.interfaces.InterfaceRevision;
import java.time.LocalDateTime;

public final class InterfaceRevisionMapper {

  private InterfaceRevisionMapper() {
  }

  public static InterfaceRevisionResponse toResponse(InterfaceRevision revision) {
    return new InterfaceRevisionResponse(
        revision.getRevisionId(),
        revision.getAnInterface() != null ? revision.getAnInterface().getInterfaceId() : null,
        revision.getVersionNo(),
        revision.getChangedBy() != null ? revision.getChangedBy().getEmail() : null,
        revision.getChangedAt(),
        revision.getSnapshot(),
        revision.getReason());
  }

  public static void applyCreate(InterfaceRevision revision,
      InterfaceRevisionCreateRequest request) {
    revision.setRevisionId(UUIDProvider.provideUUID(InterfaceRevision.TABLE_NAME));
    revision.setVersionNo(request.versionNo());
    revision.setSnapshot(request.snapshot());
    revision.setReason(request.reason());
    revision.setChangedAt(LocalDateTime.now());
  }

  public static void applyUpdate(InterfaceRevision revision,
      InterfaceRevisionUpdateRequest request) {
    if (request.versionNo() != null) {
      revision.setVersionNo(request.versionNo());
    }
    if (request.snapshot() != null) {
      revision.setSnapshot(request.snapshot());
    }
    if (request.reason() != null) {
      revision.setReason(request.reason());
    }
    revision.setChangedAt(LocalDateTime.now());
  }
}
