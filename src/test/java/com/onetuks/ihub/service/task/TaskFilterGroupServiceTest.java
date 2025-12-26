package com.onetuks.ihub.service.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.onetuks.ihub.TestcontainersConfiguration;
import com.onetuks.ihub.dto.task.TaskFilterGroupCreateRequest;
import com.onetuks.ihub.dto.task.TaskFilterGroupResponse;
import com.onetuks.ihub.dto.task.TaskFilterGroupUpdateRequest;
import com.onetuks.ihub.entity.project.Project;
import com.onetuks.ihub.entity.task.TaskFilterGroupDateType;
import com.onetuks.ihub.entity.user.User;
import com.onetuks.ihub.mapper.TaskFilterGroupMapper;
import com.onetuks.ihub.repository.ProjectJpaRepository;
import com.onetuks.ihub.repository.TaskFilterGroupJpaRepository;
import com.onetuks.ihub.repository.UserJpaRepository;
import com.onetuks.ihub.service.ServiceTestDataFactory;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class TaskFilterGroupServiceTest {

  @Autowired
  private TaskFilterGroupService taskFilterGroupService;

  @Autowired
  private TaskFilterGroupJpaRepository taskFilterGroupJpaRepository;

  @Autowired
  private ProjectJpaRepository projectJpaRepository;

  @Autowired
  private UserJpaRepository userJpaRepository;

  private Project project;
  private User user;

  @BeforeEach
  void setUp() {
    user = ServiceTestDataFactory.createUser(userJpaRepository, "group@user.com", "GroupUser");
    project = ServiceTestDataFactory.createProject(projectJpaRepository, user, user, "GroupProj");
  }

  @AfterEach
  void tearDown() {
    taskFilterGroupJpaRepository.deleteAll();
    projectJpaRepository.deleteAll();
    userJpaRepository.deleteAll();
  }

  @Test
  void createTaskFilterGroup_success() {
    TaskFilterGroupCreateRequest request = new TaskFilterGroupCreateRequest(
        user.getEmail(),
        project.getProjectId(),
        "MyGroup",
        "assignee",
        "author",
        TaskFilterGroupDateType.CREATED,
        LocalDate.now(),
        LocalDate.now().plusDays(1));

    TaskFilterGroupResponse response =
        TaskFilterGroupMapper.toResponse(taskFilterGroupService.create(request));

    assertNotNull(response.groupId());
    assertEquals("MyGroup", response.name());
    assertEquals(TaskFilterGroupDateType.CREATED, response.dateType());
  }

  @Test
  void updateTaskFilterGroup_success() {
    TaskFilterGroupResponse created = TaskFilterGroupMapper.toResponse(
        taskFilterGroupService.create(new TaskFilterGroupCreateRequest(
            user.getEmail(), project.getProjectId(), "Group", null, null,
            TaskFilterGroupDateType.CREATED, null, null)));

    TaskFilterGroupUpdateRequest updateRequest = new TaskFilterGroupUpdateRequest(
        "GroupUpdated",
        "assignee2",
        "author2",
        TaskFilterGroupDateType.DUE,
        LocalDate.now(),
        LocalDate.now().plusDays(2),
        LocalDate.now());

    TaskFilterGroupResponse updated = TaskFilterGroupMapper.toResponse(
        taskFilterGroupService.update(created.groupId(), updateRequest));

    assertEquals("GroupUpdated", updated.name());
    assertEquals(TaskFilterGroupDateType.DUE, updated.dateType());
  }

  @Test
  void getTaskFilterGroups_returnsAll() {
    taskFilterGroupService.create(new TaskFilterGroupCreateRequest(
        user.getEmail(), project.getProjectId(), "G1", null, null,
        TaskFilterGroupDateType.CREATED, null, null));
    taskFilterGroupService.create(new TaskFilterGroupCreateRequest(
        user.getEmail(), project.getProjectId(), "G2", null, null,
        TaskFilterGroupDateType.CREATED, null, null));

    assertEquals(2, taskFilterGroupService.getAll().size());
  }

  @Test
  void deleteTaskFilterGroup_success() {
    TaskFilterGroupResponse created = TaskFilterGroupMapper.toResponse(
        taskFilterGroupService.create(new TaskFilterGroupCreateRequest(
        user.getEmail(), project.getProjectId(), "G3", null, null,
        TaskFilterGroupDateType.CREATED, null, null)));

    taskFilterGroupService.delete(created.groupId());

    assertEquals(0, taskFilterGroupJpaRepository.count());
    assertThrows(EntityNotFoundException.class,
        () -> taskFilterGroupService.getById(created.groupId()));
  }
}
