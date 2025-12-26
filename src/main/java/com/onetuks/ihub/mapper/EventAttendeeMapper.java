package com.onetuks.ihub.mapper;

import com.onetuks.ihub.dto.event.EventAttendeeCreateRequest;
import com.onetuks.ihub.dto.event.EventAttendeeResponse;
import com.onetuks.ihub.dto.event.EventAttendeeUpdateRequest;
import com.onetuks.ihub.entity.event.EventAttendee;

public final class EventAttendeeMapper {

  private EventAttendeeMapper() {
  }

  public static EventAttendeeResponse toResponse(EventAttendee eventAttendee) {
    return new EventAttendeeResponse(
        eventAttendee.getEventAttendeeId(),
        eventAttendee.getEvent() != null ? eventAttendee.getEvent().getEventId() : null,
        eventAttendee.getUser() != null ? eventAttendee.getUser().getEmail() : null,
        eventAttendee.getIsMandatory(),
        eventAttendee.getAttendStatus());
  }

  public static void applyCreate(EventAttendee eventAttendee, EventAttendeeCreateRequest request) {
    eventAttendee.setEventAttendeeId(UUIDProvider.provideUUID(EventAttendee.TABLE_NAME));
    eventAttendee.setIsMandatory(request.isMandatory());
    eventAttendee.setAttendStatus(request.attendStatus());
  }

  public static void applyUpdate(EventAttendee eventAttendee, EventAttendeeUpdateRequest request) {
    if (request.isMandatory() != null) {
      eventAttendee.setIsMandatory(request.isMandatory());
    }
    if (request.attendStatus() != null) {
      eventAttendee.setAttendStatus(request.attendStatus());
    }
  }
}
