package com.onetuks.ihub.service.communication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.onetuks.ihub.TestcontainersConfiguration;
import com.onetuks.ihub.dto.communication.MentionCreateRequest;
import com.onetuks.ihub.dto.communication.MentionResponse;
import com.onetuks.ihub.dto.communication.MentionUpdateRequest;
import com.onetuks.ihub.entity.communication.TargetType;
import com.onetuks.ihub.entity.project.Project;
import com.onetuks.ihub.entity.user.User;
import com.onetuks.ihub.mapper.MentionMapper;
import com.onetuks.ihub.repository.MentionJpaRepository;
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
class MentionServiceTest {

  @Autowired
  private MentionService mentionService;

  @Autowired
  private MentionJpaRepository mentionJpaRepository;

  @Autowired
  private ProjectJpaRepository projectJpaRepository;

  @Autowired
  private UserJpaRepository userJpaRepository;

  private Project project;
  private User mentioned;
  private User creator;

  @BeforeEach
  void setUp() {
    creator = ServiceTestDataFactory.createUser(userJpaRepository, "mention@creator.com", "Creator");
    mentioned = ServiceTestDataFactory.createUser(userJpaRepository, "mention@user.com", "Mentioned");
    project = ServiceTestDataFactory.createProject(projectJpaRepository, creator, creator, "MentionProj");
  }

  @AfterEach
  void tearDown() {
    mentionJpaRepository.deleteAll();
    projectJpaRepository.deleteAll();
    userJpaRepository.deleteAll();
  }

  @Test
  void createMention_success() {
    MentionCreateRequest request = new MentionCreateRequest(
        project.getProjectId(),
        TargetType.POST,
        "1L",
        mentioned.getEmail(),
        creator.getEmail());

    MentionResponse response = MentionMapper.toResponse(mentionService.create(request));

    assertNotNull(response.mentionId());
    assertEquals(TargetType.POST, response.targetType());
    assertEquals(mentioned.getEmail(), response.mentionedUserId());
  }

  @Test
  void updateMention_success() {
    MentionResponse created = MentionMapper.toResponse(mentionService.create(new MentionCreateRequest(
        project.getProjectId(), TargetType.POST, "1L", mentioned.getEmail(), creator.getEmail())));

    MentionUpdateRequest updateRequest =
        new MentionUpdateRequest(TargetType.TASK, "2L", creator.getEmail(), mentioned.getEmail());

    MentionResponse updated = MentionMapper.toResponse(mentionService.update(created.mentionId(), updateRequest));

    assertEquals(TargetType.TASK, updated.targetType());
    assertEquals("2L", updated.targetId());
    assertEquals(creator.getEmail(), updated.mentionedUserId());
  }

  @Test
  void getMentions_returnsAll() {
    mentionService.create(new MentionCreateRequest(
        project.getProjectId(), TargetType.POST, "1L", mentioned.getEmail(), creator.getEmail()));
    mentionService.create(new MentionCreateRequest(
        project.getProjectId(), TargetType.TASK, "2L", creator.getEmail(), creator.getEmail()));

    assertEquals(2, mentionService.getAll().size());
  }

  @Test
  void deleteMention_success() {
    MentionResponse created = MentionMapper.toResponse(mentionService.create(new MentionCreateRequest(
        project.getProjectId(), TargetType.POST, "1L", mentioned.getEmail(), creator.getEmail())));

    mentionService.delete(created.mentionId());

    assertEquals(0, mentionJpaRepository.count());
    assertThrows(EntityNotFoundException.class, () -> mentionService.getById(created.mentionId()));
  }
}
