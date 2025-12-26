package com.onetuks.ihub.service.interfaces;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.onetuks.ihub.TestcontainersConfiguration;
import com.onetuks.ihub.dto.interfaces.InterfaceCreateRequest;
import com.onetuks.ihub.dto.interfaces.InterfaceResponse;
import com.onetuks.ihub.dto.interfaces.InterfaceUpdateRequest;
import com.onetuks.ihub.entity.interfaces.ChannelAdapter;
import com.onetuks.ihub.entity.interfaces.Interface;
import com.onetuks.ihub.entity.interfaces.InterfaceStatus;
import com.onetuks.ihub.entity.interfaces.InterfaceType;
import com.onetuks.ihub.entity.interfaces.SyncAsyncType;
import com.onetuks.ihub.entity.project.Project;
import com.onetuks.ihub.entity.user.User;
import com.onetuks.ihub.mapper.InterfaceMapper;
import com.onetuks.ihub.repository.InterfaceJpaRepository;
import com.onetuks.ihub.repository.InterfaceStatusJpaRepository;
import com.onetuks.ihub.repository.ProjectJpaRepository;
import com.onetuks.ihub.repository.SystemJpaRepository;
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
class InterfaceServiceTest {

  @Autowired
  private InterfaceService interfaceService;

  @Autowired
  private InterfaceJpaRepository interfaceJpaRepository;

  @Autowired
  private ProjectJpaRepository projectJpaRepository;

  @Autowired
  private SystemJpaRepository systemJpaRepository;

  @Autowired
  private InterfaceStatusJpaRepository interfaceStatusJpaRepository;

  @Autowired
  private UserJpaRepository userJpaRepository;

  private Project project;
  private User user;
  private com.onetuks.ihub.entity.system.System sourceSystem;
  private com.onetuks.ihub.entity.system.System targetSystem;
  private InterfaceStatus status;
  private InterfaceStatus newStatus;

  @BeforeEach
  void setUp() {
    user = ServiceTestDataFactory.createUser(userJpaRepository);
    project = ServiceTestDataFactory.createProject(projectJpaRepository, user, user, "IFProj");
    sourceSystem = ServiceTestDataFactory.createSystem(
        systemJpaRepository, project, user, user, "SRC");
    targetSystem = ServiceTestDataFactory.createSystem(
        systemJpaRepository, project, user, user, "TGT");
    status = ServiceTestDataFactory.createInterfaceStatus(interfaceStatusJpaRepository, project,
        "Draft", 1);
    newStatus = ServiceTestDataFactory.createInterfaceStatus(interfaceStatusJpaRepository, project,
        "Live", 2);
  }

  @AfterEach
  void tearDown() {
    interfaceJpaRepository.deleteAll();
    interfaceStatusJpaRepository.deleteAll();
    systemJpaRepository.deleteAll();
    projectJpaRepository.deleteAll();
    userJpaRepository.deleteAll();
  }

  @Test
  void createInterface_success() {
    InterfaceCreateRequest request = new InterfaceCreateRequest(
        project.getProjectId(),
        "IF-1",
        sourceSystem.getSystemId(),
        targetSystem.getSystemId(),
        "Module",
        InterfaceType.REALTIME,
        "pattern",
        com.onetuks.ihub.entity.interfaces.ChannelAdapter.HTTP,
        com.onetuks.ihub.entity.interfaces.ChannelAdapter.REST,
        SyncAsyncType.SYNC,
        status.getStatusId(),
        "batch",
        "remark",
        user.getEmail());

    InterfaceResponse response = InterfaceMapper.toResponse(interfaceService.create(request));

    assertNotNull(response.interfaceId());
    assertEquals("IF-1", response.ifId());
    assertEquals(status.getStatusId(), response.statusId());
  }

  @Test
  void updateInterface_success() {
    InterfaceResponse created = InterfaceMapper.toResponse(
        interfaceService.create(new InterfaceCreateRequest(
            project.getProjectId(), "IF-2", sourceSystem.getSystemId(), targetSystem.getSystemId(),
            "Module", InterfaceType.REALTIME, "pattern",
            com.onetuks.ihub.entity.interfaces.ChannelAdapter.HTTP,
            com.onetuks.ihub.entity.interfaces.ChannelAdapter.REST,
            SyncAsyncType.SYNC, status.getStatusId(), "batch", "remark", user.getEmail())));

    InterfaceUpdateRequest updateRequest = new InterfaceUpdateRequest(
        "IF-2-NEW",
        sourceSystem.getSystemId(),
        targetSystem.getSystemId(),
        "Module2",
        InterfaceType.BATCH,
        "pattern2",
        com.onetuks.ihub.entity.interfaces.ChannelAdapter.REST,
        com.onetuks.ihub.entity.interfaces.ChannelAdapter.HTTP,
        SyncAsyncType.ASYNC,
        newStatus.getStatusId(),
        "batch2",
        "remark2");

    InterfaceResponse updated =
        InterfaceMapper.toResponse(interfaceService.update(created.interfaceId(), updateRequest));

    assertEquals("IF-2-NEW", updated.ifId());
    assertEquals(InterfaceType.BATCH, updated.interfaceType());
    assertEquals(newStatus.getStatusId(), updated.statusId());
  }

  @Test
  void getInterfaces_returnsAll() {
    Interface anInterface = interfaceService.create(new InterfaceCreateRequest(
        project.getProjectId(), "IF-3", sourceSystem.getSystemId(), targetSystem.getSystemId(),
        "M", InterfaceType.REALTIME, "p",
        ChannelAdapter.HTTP,
        ChannelAdapter.HTTP,
        SyncAsyncType.SYNC, status.getStatusId(), "b", "r", user.getEmail()));
    Interface anInterface1 = interfaceService.create(new InterfaceCreateRequest(
        project.getProjectId(), "IF-4", sourceSystem.getSystemId(), targetSystem.getSystemId(),
        "M", InterfaceType.REALTIME, "p",
        ChannelAdapter.HTTP,
        ChannelAdapter.HTTP,
        SyncAsyncType.SYNC, status.getStatusId(), "b", "r", user.getEmail()));

    assertEquals(2, interfaceService.getAll().size());
  }

  @Test
  void deleteInterface_success() {
    InterfaceResponse created = InterfaceMapper.toResponse(
        interfaceService.create(new InterfaceCreateRequest(
            project.getProjectId(), "IF-5", sourceSystem.getSystemId(), targetSystem.getSystemId(),
            "M", InterfaceType.REALTIME, "p",
            com.onetuks.ihub.entity.interfaces.ChannelAdapter.HTTP,
            com.onetuks.ihub.entity.interfaces.ChannelAdapter.HTTP,
            SyncAsyncType.SYNC, status.getStatusId(), "b", "r", user.getEmail())));

    interfaceService.delete(created.interfaceId());

    assertEquals(0, interfaceJpaRepository.count());
    assertThrows(EntityNotFoundException.class,
        () -> interfaceService.getById(created.interfaceId()));
  }
}
