package com.onetuks.ihub.mapper;

import com.onetuks.ihub.dto.event.EventCreateRequest;
import com.onetuks.ihub.dto.event.EventResponse;
import com.onetuks.ihub.dto.event.EventUpdateRequest;
import com.onetuks.ihub.entity.event.Event;
import java.time.LocalDateTime;

public final class EventMapper {

  private EventMapper() {
  }

  public static EventResponse toResponse(Event event) {
    return new EventResponse(
        event.getEventId(),
        event.getProject() != null ? event.getProject().getProjectId() : null,
        event.getTitle(),
        event.getStartDatetime(),
        event.getEndDatetime(),
        event.getLocation(),
        event.getContent(),
        event.getRemindBeforeMinutes(),
        event.getCreatedBy() != null ? event.getCreatedBy().getEmail() : null,
        event.getCreatedAt(),
        event.getUpdatedAt());
  }

  public static void applyCreate(Event event, EventCreateRequest request) {
    LocalDateTime now = LocalDateTime.now();
    event.setEventId(UUIDProvider.provideUUID(Event.TABLE_NAME));
    event.setTitle(request.title());
    event.setStartDatetime(request.startDatetime());
    event.setEndDatetime(request.endDatetime());
    event.setLocation(request.location());
    event.setContent(request.content());
    event.setRemindBeforeMinutes(request.remindBeforeMinutes());
    event.setCreatedAt(now);
    event.setUpdatedAt(now);
  }

  public static void applyUpdate(Event event, EventUpdateRequest request) {
    if (request.title() != null) {
      event.setTitle(request.title());
    }
    if (request.startDatetime() != null) {
      event.setStartDatetime(request.startDatetime());
    }
    if (request.endDatetime() != null) {
      event.setEndDatetime(request.endDatetime());
    }
    if (request.location() != null) {
      event.setLocation(request.location());
    }
    if (request.content() != null) {
      event.setContent(request.content());
    }
    if (request.remindBeforeMinutes() != null) {
      event.setRemindBeforeMinutes(request.remindBeforeMinutes());
    }
    event.setUpdatedAt(LocalDateTime.now());
  }
}
