package com.onetuks.ihub.mapper;

import com.onetuks.ihub.dto.project.AttachmentCreateRequest;
import com.onetuks.ihub.dto.project.AttachmentResponse;
import com.onetuks.ihub.dto.project.AttachmentUpdateRequest;
import com.onetuks.ihub.entity.project.Attachment;
import java.time.LocalDateTime;

public final class AttachmentMapper {

  private AttachmentMapper() {
  }

  public static AttachmentResponse toResponse(Attachment attachment) {
    return new AttachmentResponse(
        attachment.getAttachmentId(),
        attachment.getProject() != null ? attachment.getProject().getProjectId() : null,
        attachment.getFile() != null ? attachment.getFile().getFileId() : null,
        attachment.getTargetType(),
        attachment.getTargetId(),
        attachment.getAttachedBy() != null ? attachment.getAttachedBy().getEmail() : null,
        attachment.getAttachedAt());
  }

  public static void applyCreate(Attachment attachment, AttachmentCreateRequest request) {
    attachment.setAttachmentId(UUIDProvider.provideUUID(Attachment.TABLE_NAME));
    attachment.setTargetType(request.targetType());
    attachment.setTargetId(request.targetId());
    attachment.setAttachedAt(LocalDateTime.now());
  }

  public static void applyUpdate(Attachment attachment, AttachmentUpdateRequest request) {
    if (request.targetType() != null) {
      attachment.setTargetType(request.targetType());
    }
    if (request.targetId() != null) {
      attachment.setTargetId(request.targetId());
    }
    // file and attachedBy handled in service
  }
}
