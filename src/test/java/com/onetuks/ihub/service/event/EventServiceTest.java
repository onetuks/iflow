package com.onetuks.ihub.service.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.onetuks.ihub.TestcontainersConfiguration;
import com.onetuks.ihub.dto.event.EventCreateRequest;
import com.onetuks.ihub.dto.event.EventResponse;
import com.onetuks.ihub.dto.event.EventUpdateRequest;
import com.onetuks.ihub.entity.project.Project;
import com.onetuks.ihub.entity.user.User;
import com.onetuks.ihub.mapper.EventMapper;
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
class EventServiceTest {

  @Autowired
  private EventService eventService;

  @Autowired
  private EventJpaRepository eventJpaRepository;

  @Autowired
  private ProjectJpaRepository projectJpaRepository;

  @Autowired
  private UserJpaRepository userJpaRepository;

  private Project project;
  private User creator;

  @BeforeEach
  void setUp() {
    creator = ServiceTestDataFactory.createUser(userJpaRepository);
    project = ServiceTestDataFactory.createProject(projectJpaRepository, creator, creator, "EventProj");
  }

  @AfterEach
  void tearDown() {
    eventJpaRepository.deleteAll();
    projectJpaRepository.deleteAll();
    userJpaRepository.deleteAll();
  }

  @Test
  void createEvent_success() {
    EventCreateRequest request = new EventCreateRequest(
        project.getProjectId(),
        "Kickoff",
        LocalDateTime.now(),
        LocalDateTime.now().plusHours(2),
        "Room1",
        "Content",
        30,
        creator.getEmail());

    EventResponse response = EventMapper.toResponse(eventService.create(request));

    assertNotNull(response.eventId());
    assertEquals("Kickoff", response.title());
    assertEquals(project.getProjectId(), response.projectId());
  }

  @Test
  void updateEvent_success() {
    EventResponse created = EventMapper.toResponse(eventService.create(new EventCreateRequest(
        project.getProjectId(), "Planning", LocalDateTime.now(), LocalDateTime.now().plusHours(1),
        "Room2", "desc", 15, creator.getEmail())));

    EventUpdateRequest updateRequest = new EventUpdateRequest(
        "Planning Updated",
        LocalDateTime.now().plusHours(1),
        LocalDateTime.now().plusHours(3),
        "Room3",
        "new content",
        10);

    EventResponse updated = EventMapper.toResponse(eventService.update(created.eventId(), updateRequest));

    assertEquals("Planning Updated", updated.title());
    assertEquals("Room3", updated.location());
  }

  @Test
  void getEvents_returnsAll() {
    eventService.create(new EventCreateRequest(
        project.getProjectId(), "E1", null, null, null, null, null, creator.getEmail()));
    eventService.create(new EventCreateRequest(
        project.getProjectId(), "E2", null, null, null, null, null, creator.getEmail()));

    assertEquals(2, eventService.getAll().size());
  }

  @Test
  void deleteEvent_success() {
    EventResponse created = EventMapper.toResponse(eventService.create(new EventCreateRequest(
        project.getProjectId(), "E3", null, null, null, null, null, creator.getEmail())));

    eventService.delete(created.eventId());

    assertEquals(0, eventJpaRepository.count());
    assertThrows(EntityNotFoundException.class, () -> eventService.getById(created.eventId()));
  }
}
