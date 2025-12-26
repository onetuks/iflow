package com.onetuks.ihub.service.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.onetuks.ihub.TestcontainersConfiguration;
import com.onetuks.ihub.dto.event.EventAttendeeCreateRequest;
import com.onetuks.ihub.dto.event.EventAttendeeResponse;
import com.onetuks.ihub.dto.event.EventAttendeeUpdateRequest;
import com.onetuks.ihub.dto.event.EventCreateRequest;
import com.onetuks.ihub.entity.event.EventAttendeeStatus;
import com.onetuks.ihub.entity.project.Project;
import com.onetuks.ihub.entity.user.User;
import com.onetuks.ihub.mapper.EventAttendeeMapper;
import com.onetuks.ihub.mapper.EventMapper;
import com.onetuks.ihub.repository.EventAttendeeJpaRepository;
import com.onetuks.ihub.repository.EventJpaRepository;
import com.onetuks.ihub.repository.ProjectJpaRepository;
import com.onetuks.ihub.repository.UserJpaRepository;
import com.onetuks.ihub.service.ServiceTestDataFactory;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class EventAttendeeServiceTest {

  @Autowired
  private EventAttendeeService eventAttendeeService;

  @Autowired
  private EventService eventService;

  @Autowired
  private EventAttendeeJpaRepository eventAttendeeJpaRepository;

  @Autowired
  private EventJpaRepository eventJpaRepository;

  @Autowired
  private ProjectJpaRepository projectJpaRepository;

  @Autowired
  private UserJpaRepository userJpaRepository;

  private Project project;
  private User creator;
  private User attendeeUser;
  private String eventId;

  @BeforeEach
  void setUp() {
    creator = ServiceTestDataFactory.createUser(userJpaRepository, "event.creator@test.com", "Creator");
    attendeeUser = ServiceTestDataFactory.createUser(userJpaRepository, "event.attendee@test.com", "Attendee");
    project = ServiceTestDataFactory.createProject(projectJpaRepository, creator, creator, "EventAttendeeProj");
    eventId = EventMapper.toResponse(eventService.create(new EventCreateRequest(
        project.getProjectId(), "Session", LocalDateTime.now(), LocalDateTime.now().plusHours(1),
        "Room", "desc", 5, creator.getEmail()))).eventId();
  }

  @AfterEach
  void tearDown() {
    eventAttendeeJpaRepository.deleteAll();
    eventJpaRepository.deleteAll();
    projectJpaRepository.deleteAll();
    userJpaRepository.deleteAll();
  }

  @Test
  void createEventAttendee_success() {
    EventAttendeeCreateRequest request = new EventAttendeeCreateRequest(
        eventId,
        attendeeUser.getEmail(),
        true,
        EventAttendeeStatus.ACCEPTED);

    EventAttendeeResponse response =
        EventAttendeeMapper.toResponse(eventAttendeeService.create(request));

    assertNotNull(response.eventAttendeeId());
    assertEquals(attendeeUser.getEmail(), response.userId());
    assertEquals(EventAttendeeStatus.ACCEPTED, response.attendStatus());
  }

  @Test
  void updateEventAttendee_success() {
    EventAttendeeResponse created = EventAttendeeMapper.toResponse(
        eventAttendeeService.create(new EventAttendeeCreateRequest(
            eventId, attendeeUser.getEmail(), false, EventAttendeeStatus.INVITED)));

    EventAttendeeUpdateRequest updateRequest = new EventAttendeeUpdateRequest(
        true, EventAttendeeStatus.ACCEPTED);

    EventAttendeeResponse updated = EventAttendeeMapper.toResponse(
        eventAttendeeService.update(created.eventAttendeeId(), updateRequest));

    assertEquals(true, updated.isMandatory());
    assertEquals(EventAttendeeStatus.ACCEPTED, updated.attendStatus());
  }

  @Test
  void getEventAttendees_returnsAll() {
    eventAttendeeService.create(new EventAttendeeCreateRequest(
        eventId, attendeeUser.getEmail(), false, EventAttendeeStatus.INVITED));
    eventAttendeeService.create(new EventAttendeeCreateRequest(
        eventId, creator.getEmail(), true, EventAttendeeStatus.ACCEPTED));

    assertEquals(2, eventAttendeeService.getAll().size());
  }

  @Test
  void deleteEventAttendee_success() {
    EventAttendeeResponse created = EventAttendeeMapper.toResponse(
        eventAttendeeService.create(new EventAttendeeCreateRequest(
            eventId, attendeeUser.getEmail(), false, EventAttendeeStatus.INVITED)));

    eventAttendeeService.delete(created.eventAttendeeId());

    assertEquals(0, eventAttendeeJpaRepository.count());
    assertThrows(EntityNotFoundException.class,
        () -> eventAttendeeService.getById(created.eventAttendeeId()));
  }
}
