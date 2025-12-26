package com.onetuks.ihub.mapper;

import com.onetuks.ihub.dto.communication.MentionCreateRequest;
import com.onetuks.ihub.dto.communication.MentionResponse;
import com.onetuks.ihub.dto.communication.MentionUpdateRequest;
import com.onetuks.ihub.entity.communication.Mention;
import java.time.LocalDateTime;

public final class MentionMapper {

  private MentionMapper() {
  }

  public static MentionResponse toResponse(Mention mention) {
    return new MentionResponse(
        mention.getMentionId(),
        mention.getProject() != null ? mention.getProject().getProjectId() : null,
        mention.getTargetType(),
        mention.getTargetId(),
        mention.getMentionedUser() != null ? mention.getMentionedUser().getEmail() : null,
        mention.getCreatedBy() != null ? mention.getCreatedBy().getEmail() : null,
        mention.getCreatedAt());
  }

  public static void applyCreate(Mention mention, MentionCreateRequest request) {
    mention.setMentionId(UUIDProvider.provideUUID(Mention.TABLE_NAME));
    mention.setTargetType(request.targetType());
    mention.setTargetId(request.targetId());
    mention.setCreatedAt(LocalDateTime.now());
  }

  public static void applyUpdate(Mention mention, MentionUpdateRequest request) {
    if (request.targetType() != null) {
      mention.setTargetType(request.targetType());
    }
    if (request.targetId() != null) {
      mention.setTargetId(request.targetId());
    }
  }
}
