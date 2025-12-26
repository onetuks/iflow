package com.onetuks.ihub.service.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.onetuks.ihub.TestcontainersConfiguration;
import com.onetuks.ihub.dto.project.ProjectMemberCreateRequest;
import com.onetuks.ihub.dto.project.ProjectMemberResponse;
import com.onetuks.ihub.dto.project.ProjectMemberUpdateRequest;
import com.onetuks.ihub.entity.project.Project;
import com.onetuks.ihub.entity.project.ProjectMemberRole;
import com.onetuks.ihub.entity.user.User;
import com.onetuks.ihub.mapper.ProjectMemberMapper;
import com.onetuks.ihub.repository.ProjectJpaRepository;
import com.onetuks.ihub.repository.ProjectMemberJpaRepository;
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
class ProjectMemberServiceTest {

  @Autowired
  private ProjectMemberService projectMemberService;

  @Autowired
  private ProjectMemberJpaRepository projectMemberJpaRepository;

  @Autowired
  private ProjectJpaRepository projectJpaRepository;

  @Autowired
  private UserJpaRepository userJpaRepository;

  private Project project;
  private User user;

  @BeforeEach
  void setUp() {
    user = ServiceTestDataFactory.createUser(userJpaRepository, "member@user.com", "Member");
    project = ServiceTestDataFactory.createProject(projectJpaRepository, user, user, "MemberProj");
  }

  @AfterEach
  void tearDown() {
    projectMemberJpaRepository.deleteAll();
    projectJpaRepository.deleteAll();
    userJpaRepository.deleteAll();
  }

  @Test
  void createProjectMember_success() {
    ProjectMemberCreateRequest request = new ProjectMemberCreateRequest(
        project.getProjectId(),
        user.getEmail(),
        ProjectMemberRole.PROJECT_MEMBER,
        LocalDateTime.now(),
        null);

    ProjectMemberResponse response =
        ProjectMemberMapper.toResponse(projectMemberService.create(request));

    assertNotNull(response.projectMemberId());
    assertEquals(ProjectMemberRole.PROJECT_MEMBER, response.role());
  }

  @Test
  void updateProjectMember_success() {
    ProjectMemberResponse created = ProjectMemberMapper.toResponse(projectMemberService.create(
        new ProjectMemberCreateRequest(
            project.getProjectId(), user.getEmail(), ProjectMemberRole.PROJECT_MEMBER,
            LocalDateTime.now(), null)));

    ProjectMemberUpdateRequest updateRequest =
        new ProjectMemberUpdateRequest(ProjectMemberRole.PROJECT_OWNER, LocalDateTime.now());

    ProjectMemberResponse updated = ProjectMemberMapper.toResponse(
        projectMemberService.update(created.projectMemberId(), updateRequest));

    assertEquals(ProjectMemberRole.PROJECT_OWNER, updated.role());
    assertNotNull(updated.leftAt());
  }

  @Test
  void getProjectMembers_returnsAll() {
    projectMemberService.create(new ProjectMemberCreateRequest(
        project.getProjectId(), user.getEmail(), ProjectMemberRole.PROJECT_MEMBER, null, null));
    projectMemberService.create(new ProjectMemberCreateRequest(
        project.getProjectId(), user.getEmail(), ProjectMemberRole.PROJECT_VIEWER, null, null));

    assertEquals(2, projectMemberService.getAll().size());
  }

  @Test
  void deleteProjectMember_success() {
    ProjectMemberResponse created = ProjectMemberMapper.toResponse(projectMemberService.create(
        new ProjectMemberCreateRequest(
            project.getProjectId(), user.getEmail(), ProjectMemberRole.PROJECT_MEMBER, null, null)));

    projectMemberService.delete(created.projectMemberId());

    assertEquals(0, projectMemberJpaRepository.count());
    assertThrows(EntityNotFoundException.class,
        () -> projectMemberService.getById(created.projectMemberId()));
  }
}
