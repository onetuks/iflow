package com.onetuks.ihub.entity.event;

import com.onetuks.ihub.entity.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = EventAttendee.TABLE_NAME)
@Getter
@Setter
public class EventAttendee {

  public static final String TABLE_NAME = "event_attendees";

  @Id
  @Column(name = "event_attendee_id", nullable = false)
  private String eventAttendeeId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "event_id", referencedColumnName = "event_id", nullable = false)
  private Event event;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "email", referencedColumnName = "email", nullable = false)
  private User user;

  @Column(name = "is_mandatory")
  private Boolean isMandatory;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "attend_status")
  private EventAttendeeStatus attendStatus;
}
