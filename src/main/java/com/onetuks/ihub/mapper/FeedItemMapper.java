package com.onetuks.ihub.mapper;

import com.onetuks.ihub.dto.communication.FeedItemCreateRequest;
import com.onetuks.ihub.dto.communication.FeedItemResponse;
import com.onetuks.ihub.dto.communication.FeedItemUpdateRequest;
import com.onetuks.ihub.entity.communication.FeedItem;
import java.time.LocalDateTime;

public final class FeedItemMapper {

  private FeedItemMapper() {
  }

  public static FeedItemResponse toResponse(FeedItem feedItem) {
    return new FeedItemResponse(
        feedItem.getFeedId(),
        feedItem.getProject() != null ? feedItem.getProject().getProjectId() : null,
        feedItem.getEventType(),
        feedItem.getActor() != null ? feedItem.getActor().getEmail() : null,
        feedItem.getTargetType(),
        feedItem.getTargetId(),
        feedItem.getSummary(),
        feedItem.getCreatedAt());
  }

  public static void applyCreate(FeedItem feedItem, FeedItemCreateRequest request) {
    feedItem.setFeedId(UUIDProvider.provideUUID(FeedItem.TABLE_NAME));
    feedItem.setEventType(request.eventType());
    feedItem.setTargetType(request.targetType());
    feedItem.setTargetId(request.targetId());
    feedItem.setSummary(request.summary());
    feedItem.setCreatedAt(LocalDateTime.now());
  }

  public static void applyUpdate(FeedItem feedItem, FeedItemUpdateRequest request) {
    if (request.eventType() != null) {
      feedItem.setEventType(request.eventType());
    }
    if (request.targetType() != null) {
      feedItem.setTargetType(request.targetType());
    }
    if (request.targetId() != null) {
      feedItem.setTargetId(request.targetId());
    }
    if (request.summary() != null) {
      feedItem.setSummary(request.summary());
    }
    if (request.actorId() != null) {
      // actor update handled in service
    }
  }
}
