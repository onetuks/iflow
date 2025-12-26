package com.onetuks.ihub.service.communication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.onetuks.ihub.TestcontainersConfiguration;
import com.onetuks.ihub.dto.communication.CommentCreateRequest;
import com.onetuks.ihub.dto.communication.CommentResponse;
import com.onetuks.ihub.dto.communication.CommentUpdateRequest;
import com.onetuks.ihub.entity.communication.TargetType;
import com.onetuks.ihub.entity.project.Project;
import com.onetuks.ihub.entity.user.User;
import com.onetuks.ihub.mapper.CommentMapper;
import com.onetuks.ihub.repository.CommentJpaRepository;
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
class CommentServiceTest {

  @Autowired
  private CommentService commentService;

  @Autowired
  private CommentJpaRepository commentJpaRepository;

  @Autowired
  private ProjectJpaRepository projectJpaRepository;

  @Autowired
  private UserJpaRepository userJpaRepository;

  private Project project;
  private User author;

  @BeforeEach
  void setUp() {
    author = ServiceTestDataFactory.createUser(userJpaRepository, "comment@user.com", "CommentUser");
    project = ServiceTestDataFactory.createProject(projectJpaRepository, author, author, "CommentProj");
  }

  @AfterEach
  void tearDown() {
    commentJpaRepository.deleteAll();
    projectJpaRepository.deleteAll();
    userJpaRepository.deleteAll();
  }

  @Test
  void createComment_success() {
    CommentCreateRequest request = new CommentCreateRequest(
        project.getProjectId(),
        null,
        TargetType.POST,
        "1L",
        "Hello",
        author.getEmail());

    CommentResponse response = CommentMapper.toResponse(commentService.create(request));

    assertNotNull(response.commentId());
    assertEquals("Hello", response.content());
    assertEquals(TargetType.POST, response.targetType());
  }

  @Test
  void updateComment_success() {
    CommentResponse created = CommentMapper.toResponse(commentService.create(new CommentCreateRequest(
        project.getProjectId(), null, TargetType.POST, "1L", "Old", author.getEmail())));

    CommentUpdateRequest updateRequest =
        new CommentUpdateRequest(TargetType.TASK, "2L", "Updated");

    CommentResponse updated = CommentMapper.toResponse(commentService.update(created.commentId(), updateRequest));

    assertEquals(TargetType.TASK, updated.targetType());
    assertEquals("2L", updated.targetId());
    assertEquals("Updated", updated.content());
  }

  @Test
  void getComments_returnsAll() {
    commentService.create(new CommentCreateRequest(
        project.getProjectId(), null, TargetType.POST, "1L", "A", author.getEmail()));
    commentService.create(new CommentCreateRequest(
        project.getProjectId(), null, TargetType.POST, "1L", "B", author.getEmail()));

    assertEquals(2, commentService.getAll().size());
  }

  @Test
  void deleteComment_success() {
    CommentResponse created = CommentMapper.toResponse(commentService.create(new CommentCreateRequest(
        project.getProjectId(), null, TargetType.POST, "1L", "C", author.getEmail())));

    commentService.delete(created.commentId());

    assertEquals(0, commentJpaRepository.count());
    assertThrows(EntityNotFoundException.class, () -> commentService.getById(created.commentId()));
  }
}
