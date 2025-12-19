package com.onetuks.ihub.service.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.onetuks.ihub.TestcontainersConfiguration;
import com.onetuks.ihub.dto.project.ProjectCreateRequest;
import com.onetuks.ihub.dto.project.ProjectResponse;
import com.onetuks.ihub.dto.project.ProjectUpdateRequest;
import com.onetuks.ihub.entity.project.ProjectStatus;
import com.onetuks.ihub.entity.user.User;
import com.onetuks.ihub.entity.user.UserRole;
import com.onetuks.ihub.entity.user.UserStatus;
import com.onetuks.ihub.repository.ProjectJpaRepository;
import com.onetuks.ihub.repository.UserJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class ProjectServiceTest {

  @Autowired
  private ProjectService projectService;

  @Autowired
  private ProjectJpaRepository projectJpaRepository;

  @Autowired
  private UserJpaRepository userJpaRepository;

  private User creator;
  private User admin;

  @BeforeEach
  void setUp() {
    creator = userJpaRepository.save(buildUser("creator@example.com", "Creator"));
    admin = userJpaRepository.save(buildUser("admin@example.com", "Admin"));
  }

  @AfterEach
  void tearDown() {
    projectJpaRepository.deleteAll();
    userJpaRepository.deleteAll();
  }

  @Test
  void createProject_success() {
    ProjectCreateRequest request = new ProjectCreateRequest(
        "Project A",
        "Desc",
        LocalDate.now(),
        LocalDate.now().plusDays(10),
        creator.getUserId(),
        admin.getUserId(),
        ProjectStatus.ACTIVE);

    ProjectResponse response = projectService.create(request);

    assertNotNull(response.projectId());
    assertEquals("Project A", response.title());
    assertEquals(ProjectStatus.ACTIVE, response.status());
    assertEquals(creator.getUserId(), response.createdById());
    assertEquals(admin.getUserId(), response.currentAdminId());
  }

  @Test
  void updateProject_success() {
    ProjectResponse created = projectService.create(new ProjectCreateRequest(
        "Project B",
        "Desc",
        LocalDate.now(),
        LocalDate.now().plusDays(5),
        creator.getUserId(),
        admin.getUserId(),
        ProjectStatus.ACTIVE));

    ProjectUpdateRequest updateRequest = new ProjectUpdateRequest(
        "Project B Updated",
        "New Desc",
        LocalDate.now().plusDays(1),
        LocalDate.now().plusDays(8),
        creator.getUserId(), // swap admin
        ProjectStatus.INACTIVE);

    ProjectResponse updated = projectService.update(created.projectId(), updateRequest);

    assertEquals("Project B Updated", updated.title());
    assertEquals("New Desc", updated.description());
    assertEquals(ProjectStatus.INACTIVE, updated.status());
    assertEquals(creator.getUserId(), updated.currentAdminId());
  }

  @Test
  void getProjects_returnsAll() {
    projectService.create(new ProjectCreateRequest(
        "P1", null, null, null, creator.getUserId(), admin.getUserId(), ProjectStatus.ACTIVE));
    projectService.create(new ProjectCreateRequest(
        "P2", null, null, null, creator.getUserId(), admin.getUserId(), ProjectStatus.ACTIVE));

    List<ProjectResponse> responses = projectService.getAll();

    assertEquals(2, responses.size());
  }

  @Test
  void deleteProject_success() {
    ProjectResponse created = projectService.create(new ProjectCreateRequest(
        "P3", null, null, null, creator.getUserId(), admin.getUserId(), ProjectStatus.ACTIVE));

    projectService.delete(created.projectId());

    assertEquals(0, projectJpaRepository.count());
    assertThrows(EntityNotFoundException.class, () -> projectService.getById(created.projectId()));
  }

  private User buildUser(String email, String name) {
    User user = new User();
    user.setEmail(email);
    user.setPassword("pass");
    user.setName(name);
    user.setStatus(UserStatus.ACTIVE);
    user.setRole(UserRole.EAI);
    return user;
  }
}
