package com.onetuks.ihub.service.interfaces;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.onetuks.ihub.TestcontainersConfiguration;
import com.onetuks.ihub.dto.interfaces.InterfaceCreateRequest;
import com.onetuks.ihub.dto.interfaces.InterfaceRevisionCreateRequest;
import com.onetuks.ihub.dto.interfaces.InterfaceRevisionResponse;
import com.onetuks.ihub.dto.interfaces.InterfaceRevisionUpdateRequest;
import com.onetuks.ihub.entity.interfaces.InterfaceStatus;
import com.onetuks.ihub.entity.interfaces.InterfaceType;
import com.onetuks.ihub.entity.interfaces.SyncAsyncType;
import com.onetuks.ihub.entity.project.Project;
import com.onetuks.ihub.entity.user.User;
import com.onetuks.ihub.mapper.InterfaceMapper;
import com.onetuks.ihub.mapper.InterfaceRevisionMapper;
import com.onetuks.ihub.repository.InterfaceJpaRepository;
import com.onetuks.ihub.repository.InterfaceRevisionJpaRepository;
import com.onetuks.ihub.repository.InterfaceStatusJpaRepository;
import com.onetuks.ihub.repository.ProjectJpaRepository;
import com.onetuks.ihub.repository.SystemJpaRepository;
import com.onetuks.ihub.repository.UserJpaRepository;
import com.onetuks.ihub.service.ServiceTestDataFactory;
import jakarta.persistence.EntityNotFoundException;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class InterfaceRevisionServiceTest {

  @Autowired
  private InterfaceRevisionService interfaceRevisionService;

  @Autowired
  private InterfaceService interfaceService;

  @Autowired
  private InterfaceRevisionJpaRepository interfaceRevisionJpaRepository;

  @Autowired
  private InterfaceJpaRepository interfaceJpaRepository;

  @Autowired
  private InterfaceStatusJpaRepository interfaceStatusJpaRepository;

  @Autowired
  private ProjectJpaRepository projectJpaRepository;

  @Autowired
  private SystemJpaRepository systemJpaRepository;

  @Autowired
  private UserJpaRepository userJpaRepository;

  private Project project;
  private User user;
  private com.onetuks.ihub.entity.system.System sourceSystem;
  private com.onetuks.ihub.entity.system.System targetSystem;
  private InterfaceStatus status;
  private String interfaceId;

  @BeforeEach
  void setUp() {
    user = ServiceTestDataFactory.createUser(userJpaRepository, "ifrev@user.com", "IFRevUser");
    project = ServiceTestDataFactory.createProject(projectJpaRepository, user, user, "IFRevProj");
    sourceSystem = ServiceTestDataFactory.createSystem(systemJpaRepository, project, user, user, "SRC-REV");
    targetSystem = ServiceTestDataFactory.createSystem(systemJpaRepository, project, user, user, "TGT-REV");
    status = ServiceTestDataFactory.createInterfaceStatus(interfaceStatusJpaRepository, project, "Draft", 1);
    interfaceId = InterfaceMapper.toResponse(interfaceService.create(new InterfaceCreateRequest(
        project.getProjectId(),
        "IF-REV",
        sourceSystem.getSystemId(),
        targetSystem.getSystemId(),
        "Module",
        InterfaceType.REALTIME,
        "pattern",
        com.onetuks.ihub.entity.interfaces.ChannelAdapter.HTTP,
        com.onetuks.ihub.entity.interfaces.ChannelAdapter.HTTP,
        SyncAsyncType.SYNC,
        status.getStatusId(),
        "batch",
        "remark",
        user.getEmail()))).interfaceId();
  }

  @AfterEach
  void tearDown() {
    interfaceRevisionJpaRepository.deleteAll();
    interfaceJpaRepository.deleteAll();
    interfaceStatusJpaRepository.deleteAll();
    systemJpaRepository.deleteAll();
    projectJpaRepository.deleteAll();
    userJpaRepository.deleteAll();
  }

  @Test
  void createInterfaceRevision_success() {
    InterfaceRevisionCreateRequest request = new InterfaceRevisionCreateRequest(
        interfaceId,
        1,
        user.getEmail(),
        Map.of("k", "v"),
        "reason");

    InterfaceRevisionResponse response =
        InterfaceRevisionMapper.toResponse(interfaceRevisionService.create(request));

    assertNotNull(response.revisionId());
    assertEquals(1, response.versionNo());
    assertEquals("reason", response.reason());
  }

  @Test
  void updateInterfaceRevision_success() {
    InterfaceRevisionResponse created = InterfaceRevisionMapper.toResponse(
        interfaceRevisionService.create(new InterfaceRevisionCreateRequest(
            interfaceId, 1, user.getEmail(), Map.of("k", "v"), "reason")));

    InterfaceRevisionUpdateRequest updateRequest =
        new InterfaceRevisionUpdateRequest(2, user.getEmail(), Map.of("k2", "v2"), "new reason");

    InterfaceRevisionResponse updated = InterfaceRevisionMapper.toResponse(
        interfaceRevisionService.update(created.revisionId(), updateRequest));

    assertEquals(2, updated.versionNo());
    assertEquals("new reason", updated.reason());
  }

  @Test
  void getInterfaceRevisions_returnsAll() {
    interfaceRevisionService.create(new InterfaceRevisionCreateRequest(
        interfaceId, 1, user.getEmail(), Map.of("a", "b"), "r1"));
    interfaceRevisionService.create(new InterfaceRevisionCreateRequest(
        interfaceId, 2, user.getEmail(), Map.of("c", "d"), "r2"));

    assertEquals(2, interfaceRevisionService.getAll().size());
  }

  @Test
  void deleteInterfaceRevision_success() {
    InterfaceRevisionResponse created = InterfaceRevisionMapper.toResponse(
        interfaceRevisionService.create(new InterfaceRevisionCreateRequest(
            interfaceId, 1, user.getEmail(), Map.of("a", "b"), "r1")));

    interfaceRevisionService.delete(created.revisionId());

    assertEquals(0, interfaceRevisionJpaRepository.count());
    assertThrows(EntityNotFoundException.class,
        () -> interfaceRevisionService.getById(created.revisionId()));
  }
}
