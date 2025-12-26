package com.onetuks.ihub.service.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.onetuks.ihub.TestcontainersConfiguration;
import com.onetuks.ihub.dto.task.TaskFilterGroupCreateRequest;
import com.onetuks.ihub.dto.task.TaskFilterGroupResponse;
import com.onetuks.ihub.dto.task.TaskFilterGroupStatusCreateRequest;
import com.onetuks.ihub.dto.task.TaskFilterGroupStatusResponse;
import com.onetuks.ihub.dto.task.TaskFilterGroupStatusUpdateRequest;
import com.onetuks.ihub.entity.task.TaskFilterGroupDateType;
import com.onetuks.ihub.entity.task.TaskFilterGroupStatusType;
import com.onetuks.ihub.entity.user.User;
import com.onetuks.ihub.mapper.TaskFilterGroupMapper;
import com.onetuks.ihub.mapper.TaskFilterGroupStatusMapper;
import com.onetuks.ihub.repository.ProjectJpaRepository;
import com.onetuks.ihub.repository.TaskFilterGroupJpaRepository;
import com.onetuks.ihub.repository.TaskFilterGroupStatusJpaRepository;
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
class TaskFilterGroupStatusServiceTest {

  @Autowired
  private TaskFilterGroupStatusService taskFilterGroupStatusService;

  @Autowired
  private TaskFilterGroupService taskFilterGroupService;

  @Autowired
  private TaskFilterGroupStatusJpaRepository taskFilterGroupStatusJpaRepository;

  @Autowired
  private TaskFilterGroupJpaRepository taskFilterGroupJpaRepository;

  @Autowired
  private ProjectJpaRepository projectJpaRepository;

  @Autowired
  private UserJpaRepository userJpaRepository;

  private TaskFilterGroupResponse group;
  private User user;

  @BeforeEach
  void setUp() {
    user = ServiceTestDataFactory.createUser(userJpaRepository, "groupstatus@user.com", "GSUser");
    var project = ServiceTestDataFactory.createProject(projectJpaRepository, user, user, "GSProj");
    group = TaskFilterGroupMapper.toResponse(taskFilterGroupService.create(
        new TaskFilterGroupCreateRequest(
            user.getEmail(), project.getProjectId(), "Group", null, null,
            TaskFilterGroupDateType.CREATED, null, null)));
  }

  @AfterEach
  void tearDown() {
    taskFilterGroupStatusJpaRepository.deleteAll();
    taskFilterGroupJpaRepository.deleteAll();
    projectJpaRepository.deleteAll();
    userJpaRepository.deleteAll();
  }

  @Test
  void createTaskFilterGroupStatus_success() {
    TaskFilterGroupStatusCreateRequest request = new TaskFilterGroupStatusCreateRequest(
        group.groupId(),
        TaskFilterGroupStatusType.REQUEST);

    TaskFilterGroupStatusResponse response =
        TaskFilterGroupStatusMapper.toResponse(taskFilterGroupStatusService.create(request));

    assertNotNull(response.statusId());
    assertEquals(TaskFilterGroupStatusType.REQUEST, response.statusType());
  }

  @Test
  void updateTaskFilterGroupStatus_success() {
    TaskFilterGroupStatusResponse created = TaskFilterGroupStatusMapper.toResponse(
        taskFilterGroupStatusService.create(
        new TaskFilterGroupStatusCreateRequest(
            group.groupId(), TaskFilterGroupStatusType.REQUEST)));

    TaskFilterGroupStatusUpdateRequest updateRequest =
        new TaskFilterGroupStatusUpdateRequest(TaskFilterGroupStatusType.IN_PROGRESS);

    TaskFilterGroupStatusResponse updated = TaskFilterGroupStatusMapper.toResponse(
        taskFilterGroupStatusService.update(created.statusId(), updateRequest));

    assertEquals(TaskFilterGroupStatusType.IN_PROGRESS, updated.statusType());
  }

  @Test
  void getTaskFilterGroupStatuses_returnsAll() {
    taskFilterGroupStatusService.create(new TaskFilterGroupStatusCreateRequest(
        group.groupId(), TaskFilterGroupStatusType.REQUEST));
    taskFilterGroupStatusService.create(new TaskFilterGroupStatusCreateRequest(
        group.groupId(), TaskFilterGroupStatusType.COMPLETED));

    assertEquals(2, taskFilterGroupStatusService.getAll().size());
  }

  @Test
  void deleteTaskFilterGroupStatus_success() {
    TaskFilterGroupStatusResponse created = TaskFilterGroupStatusMapper.toResponse(
        taskFilterGroupStatusService.create(
        new TaskFilterGroupStatusCreateRequest(
            group.groupId(), TaskFilterGroupStatusType.REQUEST)));

    taskFilterGroupStatusService.delete(created.statusId());

    assertEquals(0, taskFilterGroupStatusJpaRepository.count());
    assertThrows(EntityNotFoundException.class,
        () -> taskFilterGroupStatusService.getById(created.statusId()));
  }
}
