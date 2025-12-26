package com.onetuks.ihub.service.system;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.onetuks.ihub.TestcontainersConfiguration;
import com.onetuks.ihub.dto.system.SystemCreateRequest;
import com.onetuks.ihub.dto.system.SystemResponse;
import com.onetuks.ihub.dto.system.SystemUpdateRequest;
import com.onetuks.ihub.entity.project.Project;
import com.onetuks.ihub.entity.system.SystemEnvironment;
import com.onetuks.ihub.entity.system.SystemStatus;
import com.onetuks.ihub.entity.system.SystemType;
import com.onetuks.ihub.entity.user.User;
import com.onetuks.ihub.mapper.SystemMapper;
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
class SystemServiceTest {

  @Autowired
  private SystemService systemService;

  @Autowired
  private SystemJpaRepository systemJpaRepository;

  @Autowired
  private ProjectJpaRepository projectJpaRepository;

  @Autowired
  private UserJpaRepository userJpaRepository;

  private User creator;
  private User updater;
  private Project project;

  @BeforeEach
  void setUp() {
    creator = ServiceTestDataFactory.createUser(userJpaRepository);
    updater = ServiceTestDataFactory.createUser(userJpaRepository);
    project = ServiceTestDataFactory.createProject(projectJpaRepository, creator, updater, "SysProj");
  }

  @AfterEach
  void tearDown() {
    systemJpaRepository.deleteAll();
    projectJpaRepository.deleteAll();
    userJpaRepository.deleteAll();
  }

  @Test
  void createSystem_success() {
    SystemCreateRequest request = new SystemCreateRequest(
        project.getProjectId(),
        "SYS1",
        SystemStatus.ACTIVE,
        "desc",
        SystemType.DB,
        SystemEnvironment.DEV,
        creator.getEmail(),
        updater.getEmail());

    SystemResponse response = SystemMapper.toResponse(systemService.create(request));

    assertNotNull(response.systemId());
    assertEquals("SYS1", response.systemCode());
    assertEquals(SystemStatus.ACTIVE, response.status());
    assertEquals(SystemType.DB, response.systemType());
    assertEquals(project.getProjectId(), response.projectId());
  }

  @Test
  void updateSystem_success() {
    SystemResponse created = SystemMapper.toResponse(systemService.create(new SystemCreateRequest(
        project.getProjectId(),
        "SYS2",
        SystemStatus.ACTIVE,
        "desc",
        SystemType.SAP,
        SystemEnvironment.DEV,
        creator.getEmail(),
        updater.getEmail())));

    SystemUpdateRequest updateRequest = new SystemUpdateRequest(
        "SYS2-NEW",
        SystemStatus.INACTIVE,
        "updated",
        SystemType.SERVER,
        SystemEnvironment.QA,
        creator.getEmail());

    SystemResponse updated = SystemMapper.toResponse(systemService.update(created.systemId(), updateRequest));

    assertEquals("SYS2-NEW", updated.systemCode());
    assertEquals(SystemStatus.INACTIVE, updated.status());
    assertEquals(SystemEnvironment.QA, updated.environment());
  }

  @Test
  void getSystems_returnsAll() {
    systemService.create(new SystemCreateRequest(
        project.getProjectId(), "S1", SystemStatus.ACTIVE, null,
        SystemType.DB, SystemEnvironment.DEV, creator.getEmail(), updater.getEmail()));
    systemService.create(new SystemCreateRequest(
        project.getProjectId(), "S2", SystemStatus.ACTIVE, null,
        SystemType.DB, SystemEnvironment.DEV, creator.getEmail(), updater.getEmail()));

    assertEquals(2, systemService.getAll().size());
  }

  @Test
  void deleteSystem_success() {
    SystemResponse created = SystemMapper.toResponse(systemService.create(new SystemCreateRequest(
        project.getProjectId(), "S3", SystemStatus.ACTIVE, null,
        SystemType.DB, SystemEnvironment.DEV, creator.getEmail(), updater.getEmail())));

    systemService.delete(created.systemId());

    assertEquals(0, systemJpaRepository.count());
    assertThrows(EntityNotFoundException.class, () -> systemService.getById(created.systemId()));
  }
}
