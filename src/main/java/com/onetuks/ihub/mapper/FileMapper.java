package com.onetuks.ihub.mapper;

import com.onetuks.ihub.dto.file.FileCreateRequest;
import com.onetuks.ihub.dto.file.FileResponse;
import com.onetuks.ihub.dto.file.FileUpdateRequest;
import com.onetuks.ihub.entity.file.File;
import java.time.LocalDateTime;

public final class FileMapper {

  private FileMapper() {
  }

  public static FileResponse toResponse(File file) {
    return new FileResponse(
        file.getFileId(),
        file.getProject() != null ? file.getProject().getProjectId() : null,
        file.getFolder() != null ? file.getFolder().getFolderId() : null,
        file.getStatus(),
        file.getOriginalName(),
        file.getStoredName(),
        file.getSizeBytes(),
        file.getMimeType(),
        file.getUploadedBy() != null ? file.getUploadedBy().getEmail() : null,
        file.getUploadedAt(),
        file.getDeletedAt());
  }

  public static void applyCreate(File file, FileCreateRequest request) {
    file.setFileId(UUIDProvider.provideUUID(File.TABLE_NAME));
    file.setStatus(request.status());
    file.setOriginalName(request.originalName());
    file.setStoredName(request.storedName());
    file.setSizeBytes(request.sizeBytes());
    file.setMimeType(request.mimeType());
    file.setUploadedAt(LocalDateTime.now());
  }

  public static void applyUpdate(File file, FileUpdateRequest request) {
    if (request.status() != null) {
      file.setStatus(request.status());
    }
    if (request.originalName() != null) {
      file.setOriginalName(request.originalName());
    }
    if (request.storedName() != null) {
      file.setStoredName(request.storedName());
    }
    if (request.sizeBytes() != null) {
      file.setSizeBytes(request.sizeBytes());
    }
    if (request.mimeType() != null) {
      file.setMimeType(request.mimeType());
    }
    if (request.deletedAt() != null) {
      file.setDeletedAt(request.deletedAt());
    }
  }
}
