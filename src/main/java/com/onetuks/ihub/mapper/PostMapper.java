package com.onetuks.ihub.mapper;

import com.onetuks.ihub.dto.communication.PostCreateRequest;
import com.onetuks.ihub.dto.communication.PostResponse;
import com.onetuks.ihub.dto.communication.PostUpdateRequest;
import com.onetuks.ihub.entity.communication.Post;
import java.time.LocalDateTime;

public final class PostMapper {

  private PostMapper() {
  }

  public static PostResponse toResponse(Post post) {
    return new PostResponse(
        post.getPostId(),
        post.getProject() != null ? post.getProject().getProjectId() : null,
        post.getTitle(),
        post.getContent(),
        post.getCreatedBy() != null ? post.getCreatedBy().getEmail() : null,
        post.getCreatedAt(),
        post.getUpdatedAt());
  }

  public static void applyCreate(Post post, PostCreateRequest request) {
    LocalDateTime now = LocalDateTime.now();
    post.setPostId(UUIDProvider.provideUUID(Post.TABLE_NAME));
    post.setTitle(request.title());
    post.setContent(request.content());
    post.setCreatedAt(now);
    post.setUpdatedAt(now);
  }

  public static void applyUpdate(Post post, PostUpdateRequest request) {
    if (request.title() != null) {
      post.setTitle(request.title());
    }
    if (request.content() != null) {
      post.setContent(request.content());
    }
    post.setUpdatedAt(LocalDateTime.now());
  }
}
