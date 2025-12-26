package com.onetuks.ihub.mapper;

import com.onetuks.ihub.dto.communication.CommentCreateRequest;
import com.onetuks.ihub.dto.communication.CommentResponse;
import com.onetuks.ihub.dto.communication.CommentUpdateRequest;
import com.onetuks.ihub.entity.communication.Comment;
import java.time.LocalDateTime;

public final class CommentMapper {

  private CommentMapper() {
  }

  public static CommentResponse toResponse(Comment comment) {
    return new CommentResponse(
        comment.getCommentId(),
        comment.getProject() != null ? comment.getProject().getProjectId() : null,
        comment.getParentComment() != null ? comment.getParentComment().getCommentId() : null,
        comment.getTargetType(),
        comment.getTargetId(),
        comment.getContent(),
        comment.getCreatedBy() != null ? comment.getCreatedBy().getEmail() : null,
        comment.getCreatedAt(),
        comment.getUpdatedAt());
  }

  public static void applyCreate(Comment comment, CommentCreateRequest request) {
    LocalDateTime now = LocalDateTime.now();
    comment.setCommentId(UUIDProvider.provideUUID(Comment.TABLE_NAME));
    comment.setTargetType(request.targetType());
    comment.setTargetId(request.targetId());
    comment.setContent(request.content());
    comment.setCreatedAt(now);
    comment.setUpdatedAt(now);
  }

  public static void applyUpdate(Comment comment, CommentUpdateRequest request) {
    if (request.targetType() != null) {
      comment.setTargetType(request.targetType());
    }
    if (request.targetId() != null) {
      comment.setTargetId(request.targetId());
    }
    if (request.content() != null) {
      comment.setContent(request.content());
    }
    comment.setUpdatedAt(LocalDateTime.now());
  }
}
