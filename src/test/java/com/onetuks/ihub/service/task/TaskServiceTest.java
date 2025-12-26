package com.onetuks.ihub.service.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.onetuks.ihub.TestcontainersConfiguration;
import com.onetuks.ihub.dto.task.TaskCreateRequest;
import com.onetuks.ihub.dto.task.TaskResponse;
import com.onetuks.ihub.dto.task.TaskUpdateRequest;
import com.onetuks.ihub.entity.project.Project;
import com.onetuks.ihub.entity.task.TaskPriority;
import com.onetuks.ihub.entity.task.TaskStatus;
import com.onetuks.ihub.entity.task.TaskType;
import com.onetuks.ihub.entity.user.User;
import com.onetuks.ihub.mapper.TaskMapper;
import com.onetuks.ihub.repository.ProjectJpaRepository;
import com.onetuks.ihub.repository.TaskJpaRepository;
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
class TaskServiceTest {

  @Autowired
  private TaskService taskService;

  @Autowired
  private TaskJpaRepository taskJpaRepository;

  @Autowired
  private ProjectJpaRepository projectJpaRepository;

  @Autowired
  private UserJpaRepository userJpaRepository;

  private Project project;
  private User user;

  @BeforeEach
  void setUp() {
    user = ServiceTestDataFactory.createUser(userJpaRepository, "task@user.com", "TaskUser");
    project = ServiceTestDataFactory.createProject(projectJpaRepository, user, user, "TaskProj");
  }

  @AfterEach
  void tearDown() {
    taskJpaRepository.deleteAll();
    projectJpaRepository.deleteAll();
    userJpaRepository.deleteAll();
  }

  @Test
  void createTask_success() {
    TaskCreateRequest request = new TaskCreateRequest(
        project.getProjectId(),
        null,
        TaskType.PARENT,
        null,
        "Task 1",
        "desc",
        TaskStatus.REQUEST,
        user.getEmail(),
        user.getEmail(),
        LocalDate.now(),
        LocalDate.now().plusDays(1),
        TaskPriority.HIGH,
        10,
        user.getEmail());

    TaskResponse response = TaskMapper.toResponse(taskService.create(request));

    assertNotNull(response.taskId());
    assertEquals("Task 1", response.title());
    assertEquals(TaskPriority.HIGH, response.priority());
  }

  @Test
  void updateTask_success() {
    TaskResponse created = TaskMapper.toResponse(taskService.create(new TaskCreateRequest(
        project.getProjectId(), null, TaskType.PARENT, null, "Task 2", null, TaskStatus.REQUEST,
        user.getEmail(), user.getEmail(), null, null, TaskPriority.MEDIUM, 0, user.getEmail())));

    TaskUpdateRequest updateRequest = new TaskUpdateRequest(
        null,
        TaskType.GENERAL_CHILD,
        null,
        "Task 2 Updated",
        "new desc",
        TaskStatus.IN_PROGRESS,
        user.getEmail(),
        user.getEmail(),
        LocalDate.now(),
        LocalDate.now().plusDays(2),
        TaskPriority.LOW,
        50);

    TaskResponse updated = TaskMapper.toResponse(taskService.update(created.taskId(), updateRequest));

    assertEquals(TaskType.GENERAL_CHILD, updated.taskType());
    assertEquals(TaskStatus.IN_PROGRESS, updated.status());
    assertEquals(50, updated.progress());
  }

  @Test
  void getTasks_returnsAll() {
    taskService.create(new TaskCreateRequest(
        project.getProjectId(), null, TaskType.PARENT, null, "T1", null, TaskStatus.REQUEST,
        null, null, null, null, null, null, null));
    taskService.create(new TaskCreateRequest(
        project.getProjectId(), null, TaskType.PARENT, null, "T2", null, TaskStatus.REQUEST,
        null, null, null, null, null, null, null));

    assertEquals(2, taskService.getAll().size());
  }

  @Test
  void deleteTask_success() {
    TaskResponse created = TaskMapper.toResponse(taskService.create(new TaskCreateRequest(
        project.getProjectId(), null, TaskType.PARENT, null, "T3", null, TaskStatus.REQUEST,
        null, null, null, null, null, null, null)));

    taskService.delete(created.taskId());

    assertEquals(0, taskJpaRepository.count());
    assertThrows(EntityNotFoundException.class, () -> taskService.getById(created.taskId()));
  }
}
