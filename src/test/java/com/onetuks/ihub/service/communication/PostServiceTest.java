package com.onetuks.ihub.service.communication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.onetuks.ihub.TestcontainersConfiguration;
import com.onetuks.ihub.dto.communication.PostCreateRequest;
import com.onetuks.ihub.dto.communication.PostResponse;
import com.onetuks.ihub.dto.communication.PostUpdateRequest;
import com.onetuks.ihub.entity.project.Project;
import com.onetuks.ihub.entity.user.User;
import com.onetuks.ihub.mapper.PostMapper;
import com.onetuks.ihub.repository.PostJpaRepository;
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
class PostServiceTest {

  @Autowired
  private PostService postService;

  @Autowired
  private PostJpaRepository postJpaRepository;

  @Autowired
  private ProjectJpaRepository projectJpaRepository;

  @Autowired
  private UserJpaRepository userJpaRepository;

  private Project project;
  private User author;

  @BeforeEach
  void setUp() {
    author = ServiceTestDataFactory.createUser(userJpaRepository);
    project = ServiceTestDataFactory.createProject(projectJpaRepository, author, author, "PostProj");
  }

  @AfterEach
  void tearDown() {
    postJpaRepository.deleteAll();
    projectJpaRepository.deleteAll();
    userJpaRepository.deleteAll();
  }

  @Test
  void createPost_success() {
    PostCreateRequest request = new PostCreateRequest(
        project.getProjectId(),
        "Title",
        "Content",
        author.getEmail());

    PostResponse response = PostMapper.toResponse(postService.create(request));

    assertNotNull(response.postId());
    assertEquals("Title", response.title());
    assertEquals(project.getProjectId(), response.projectId());
  }

  @Test
  void updatePost_success() {
    PostResponse created = PostMapper.toResponse(postService.create(new PostCreateRequest(
        project.getProjectId(), "Old", "Old content", author.getEmail())));

    PostUpdateRequest updateRequest = new PostUpdateRequest("New", "New content");

    PostResponse updated = PostMapper.toResponse(postService.update(created.postId(), updateRequest));

    assertEquals("New", updated.title());
    assertEquals("New content", updated.content());
  }

  @Test
  void getPosts_returnsAll() {
    postService.create(new PostCreateRequest(
        project.getProjectId(), "P1", "C1", author.getEmail()));
    postService.create(new PostCreateRequest(
        project.getProjectId(), "P2", "C2", author.getEmail()));

    assertEquals(2, postService.getAll().size());
  }

  @Test
  void deletePost_success() {
    PostResponse created = PostMapper.toResponse(postService.create(new PostCreateRequest(
        project.getProjectId(), "P3", "C3", author.getEmail())));

    postService.delete(created.postId());

    assertEquals(0, postJpaRepository.count());
    assertThrows(EntityNotFoundException.class, () -> postService.getById(created.postId()));
  }
}
