package com.onetuks.ihub.service.interfaces;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.onetuks.ihub.TestcontainersConfiguration;
import com.onetuks.ihub.dto.interfaces.InterfaceCreateRequest;
import com.onetuks.ihub.dto.interfaces.InterfaceStatusHistoryCreateRequest;
import com.onetuks.ihub.dto.interfaces.InterfaceStatusHistoryResponse;
import com.onetuks.ihub.dto.interfaces.InterfaceStatusHistoryUpdateRequest;
import com.onetuks.ihub.entity.interfaces.InterfaceStatus;
import com.onetuks.ihub.entity.interfaces.InterfaceType;
import com.onetuks.ihub.entity.interfaces.SyncAsyncType;
import com.onetuks.ihub.entity.project.Project;
import com.onetuks.ihub.entity.task.Task;
import com.onetuks.ihub.entity.user.User;
import com.onetuks.ihub.mapper.InterfaceMapper;
import com.onetuks.ihub.mapper.InterfaceStatusHistoryMapper;
import com.onetuks.ihub.repository.InterfaceStatusHistoryJpaRepository;
import com.onetuks.ihub.repository.InterfaceStatusJpaRepository;
import com.onetuks.ihub.repository.ProjectJpaRepository;
import com.onetuks.ihub.repository.SystemJpaRepository;
import com.onetuks.ihub.repository.TaskJpaRepository;
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
class InterfaceStatusHistoryServiceTest {

  @Autowired
  private InterfaceStatusHistoryService interfaceStatusHistoryService;

  @Autowired
  private InterfaceService interfaceService;

  @Autowired
  private InterfaceStatusHistoryJpaRepository interfaceStatusHistoryJpaRepository;

  @Autowired
  private InterfaceStatusJpaRepository interfaceStatusJpaRepository;

  @Autowired
  private TaskJpaRepository taskJpaRepository;

  @Autowired
  private ProjectJpaRepository projectJpaRepository;

  @Autowired
  private SystemJpaRepository systemJpaRepository;

  @Autowired
  private UserJpaRepository userJpaRepository;

  private Project project;
  private User user;
  private InterfaceStatus fromStatus;
  private InterfaceStatus toStatus;
  private String interfaceId;
  private Task task;

  @BeforeEach
  void setUp() {
    user = ServiceTestDataFactory.createUser(userJpaRepository, "ifhist@user.com", "IFHist");
    project = ServiceTestDataFactory.createProject(projectJpaRepository, user, user, "IFHistProj");
    var sourceSystem =
        ServiceTestDataFactory.createSystem(systemJpaRepository, project, user, user, "SRC-H");
    var targetSystem =
        ServiceTestDataFactory.createSystem(systemJpaRepository, project, user, user, "TGT-H");
    fromStatus =
        ServiceTestDataFactory.createInterfaceStatus(interfaceStatusJpaRepository, project, "Draft", 1);
    toStatus =
        ServiceTestDataFactory.createInterfaceStatus(interfaceStatusJpaRepository, project, "Review", 2);
    interfaceId = InterfaceMapper.toResponse(interfaceService.create(new InterfaceCreateRequest(
        project.getProjectId(), "IF-H", sourceSystem.getSystemId(), targetSystem.getSystemId(),
        "M", InterfaceType.REALTIME, "p",
        com.onetuks.ihub.entity.interfaces.ChannelAdapter.HTTP,
        com.onetuks.ihub.entity.interfaces.ChannelAdapter.HTTP,
        SyncAsyncType.SYNC, fromStatus.getStatusId(), "b", "r", user.getEmail()))).interfaceId();
    task = ServiceTestDataFactory.createTask(taskJpaRepository, project, user, "TaskH");
  }

  @AfterEach
  void tearDown() {
    interfaceStatusHistoryJpaRepository.deleteAll();
    taskJpaRepository.deleteAll();
    interfaceStatusJpaRepository.deleteAll();
    systemJpaRepository.deleteAll();
    projectJpaRepository.deleteAll();
    userJpaRepository.deleteAll();
  }

  @Test
  void createInterfaceStatusHistory_success() {
    InterfaceStatusHistoryCreateRequest request = new InterfaceStatusHistoryCreateRequest(
        interfaceId,
        fromStatus.getStatusId(),
        toStatus.getStatusId(),
        user.getEmail(),
        task.getTaskId(),
        "reason");

    InterfaceStatusHistoryResponse response =
        InterfaceStatusHistoryMapper.toResponse(interfaceStatusHistoryService.create(request));

    assertNotNull(response.historyId());
    assertEquals(fromStatus.getStatusId(), response.fromStatusId());
    assertEquals(toStatus.getStatusId(), response.toStatusId());
  }

  @Test
  void updateInterfaceStatusHistory_success() {
    InterfaceStatusHistoryResponse created = InterfaceStatusHistoryMapper.toResponse(
        interfaceStatusHistoryService.create(
            new InterfaceStatusHistoryCreateRequest(
                interfaceId, fromStatus.getStatusId(), toStatus.getStatusId(), user.getEmail(),
                task.getTaskId(), "reason")));
    InterfaceStatusHistoryUpdateRequest updateRequest =
        new InterfaceStatusHistoryUpdateRequest(toStatus.getStatusId(), "new reason");

    InterfaceStatusHistoryResponse updated = InterfaceStatusHistoryMapper.toResponse(
        interfaceStatusHistoryService.update(created.historyId(), updateRequest));

    assertEquals("new reason", updated.reason());
  }

  @Test
  void getInterfaceStatusHistories_returnsAll() {
    interfaceStatusHistoryService.create(new InterfaceStatusHistoryCreateRequest(
        interfaceId, fromStatus.getStatusId(), toStatus.getStatusId(), user.getEmail(),
        task.getTaskId(), "r1"));
    interfaceStatusHistoryService.create(new InterfaceStatusHistoryCreateRequest(
        interfaceId, toStatus.getStatusId(), fromStatus.getStatusId(), user.getEmail(),
        task.getTaskId(), "r2"));

    assertEquals(2, interfaceStatusHistoryService.getAll().size());
  }

  @Test
  void deleteInterfaceStatusHistory_success() {
    InterfaceStatusHistoryResponse created = InterfaceStatusHistoryMapper.toResponse(
        interfaceStatusHistoryService.create(
            new InterfaceStatusHistoryCreateRequest(
                interfaceId, fromStatus.getStatusId(), toStatus.getStatusId(), user.getEmail(),
                task.getTaskId(), "r1")));

    interfaceStatusHistoryService.delete(created.historyId());

    assertEquals(0, interfaceStatusHistoryJpaRepository.count());
    assertThrows(EntityNotFoundException.class,
        () -> interfaceStatusHistoryService.getById(created.historyId()));
  }
}
