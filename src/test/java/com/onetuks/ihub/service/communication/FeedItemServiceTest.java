package com.onetuks.ihub.service.communication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.onetuks.ihub.TestcontainersConfiguration;
import com.onetuks.ihub.dto.communication.FeedItemCreateRequest;
import com.onetuks.ihub.dto.communication.FeedItemResponse;
import com.onetuks.ihub.dto.communication.FeedItemUpdateRequest;
import com.onetuks.ihub.entity.communication.TargetType;
import com.onetuks.ihub.entity.project.Project;
import com.onetuks.ihub.entity.user.User;
import com.onetuks.ihub.mapper.FeedItemMapper;
import com.onetuks.ihub.repository.FeedItemJpaRepository;
import com.onetuks.ihub.repository.ProjectJpaRepository;
import com.onetuks.ihub.repository.UserJpaRepository;
import com.onetuks.ihub.service.ServiceTestDataFactory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class FeedItemServiceTest {

  @Autowired
  private FeedItemService feedItemService;

  @Autowired
  private FeedItemJpaRepository feedItemJpaRepository;

  @Autowired
  private ProjectJpaRepository projectJpaRepository;

  @Autowired
  private UserJpaRepository userJpaRepository;

  private Project project;
  private User actor;
  private User newActor;

  @BeforeEach
  void setUp() {
    actor = ServiceTestDataFactory.createUser(userJpaRepository, "feed@actor.com", "Actor");
    newActor = ServiceTestDataFactory.createUser(userJpaRepository, "feed@actor2.com", "Actor2");
    project = ServiceTestDataFactory.createProject(projectJpaRepository, actor, actor, "FeedProj");
  }

  @AfterEach
  void tearDown() {
    feedItemJpaRepository.deleteAll();
    projectJpaRepository.deleteAll();
    userJpaRepository.deleteAll();
  }

  @Test
  void createFeedItem_success() {
    FeedItemCreateRequest request = new FeedItemCreateRequest(
        project.getProjectId(),
        "CREATE",
        actor.getEmail(),
        TargetType.POST,
        "1L",
        "summary");

    FeedItemResponse response = FeedItemMapper.toResponse(feedItemService.create(request));

    assertNotNull(response.feedId());
    assertEquals("CREATE", response.eventType());
    assertEquals(actor.getEmail(), response.actorId());
  }

  @Test
  void updateFeedItem_success() {
    FeedItemResponse created = FeedItemMapper.toResponse(feedItemService.create(new FeedItemCreateRequest(
        project.getProjectId(), "EVT", actor.getEmail(), TargetType.POST, "1L", "sum")));

    FeedItemUpdateRequest updateRequest = new FeedItemUpdateRequest(
        "UPDATED",
        newActor.getEmail(),
        TargetType.TASK,
        "2L",
        "updated summary");

    FeedItemResponse updated = FeedItemMapper.toResponse(feedItemService.update(created.feedId(), updateRequest));

    assertEquals("UPDATED", updated.eventType());
    assertEquals(TargetType.TASK, updated.targetType());
    assertEquals(newActor.getEmail(), updated.actorId());
  }

  @Test
  void getFeedItems_returnsAll() {
    feedItemService.create(new FeedItemCreateRequest(
        project.getProjectId(), "A", actor.getEmail(), TargetType.POST, "1L", "s1"));
    feedItemService.create(new FeedItemCreateRequest(
        project.getProjectId(), "B", actor.getEmail(), TargetType.POST, "1L", "s2"));

    assertEquals(2, feedItemService.getAll().size());
  }

  @Test
  void deleteFeedItem_success() {
    FeedItemResponse created = FeedItemMapper.toResponse(feedItemService.create(new FeedItemCreateRequest(
        project.getProjectId(), "C", actor.getEmail(), TargetType.POST, "1L", "s3")));

    feedItemService.delete(created.feedId());

    assertEquals(0, feedItemJpaRepository.count());
    assertThrows(EntityNotFoundException.class, () -> feedItemService.getById(created.feedId()));
  }
}
