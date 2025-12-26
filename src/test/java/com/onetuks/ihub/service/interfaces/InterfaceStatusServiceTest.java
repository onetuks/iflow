package com.onetuks.ihub.service.interfaces;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.onetuks.ihub.TestcontainersConfiguration;
import com.onetuks.ihub.dto.interfaces.InterfaceStatusCreateRequest;
import com.onetuks.ihub.dto.interfaces.InterfaceStatusResponse;
import com.onetuks.ihub.dto.interfaces.InterfaceStatusUpdateRequest;
import com.onetuks.ihub.entity.project.Project;
import com.onetuks.ihub.entity.user.User;
import com.onetuks.ihub.mapper.InterfaceStatusMapper;
import com.onetuks.ihub.repository.InterfaceStatusJpaRepository;
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
class InterfaceStatusServiceTest {

  @Autowired
  private InterfaceStatusService interfaceStatusService;

  @Autowired
  private InterfaceStatusJpaRepository interfaceStatusJpaRepository;

  @Autowired
  private ProjectJpaRepository projectJpaRepository;

  @Autowired
  private UserJpaRepository userJpaRepository;

  private Project project;
  private User user;

  @BeforeEach
  void setUp() {
    user = ServiceTestDataFactory.createUser(userJpaRepository);
    project = ServiceTestDataFactory.createProject(projectJpaRepository, user, user, "IFStatusProj");
  }

  @AfterEach
  void tearDown() {
    interfaceStatusJpaRepository.deleteAll();
    projectJpaRepository.deleteAll();
    userJpaRepository.deleteAll();
  }

  @Test
  void createInterfaceStatus_success() {
    InterfaceStatusCreateRequest request = new InterfaceStatusCreateRequest(
        project.getProjectId(),
        "Ready",
        "RD",
        1,
        true);

    InterfaceStatusResponse response = InterfaceStatusMapper.toResponse(
        interfaceStatusService.create(request));

    assertNotNull(response.statusId());
    assertEquals("Ready", response.name());
    assertEquals(1, response.seqOrder());
  }

  @Test
  void updateInterfaceStatus_success() {
    InterfaceStatusResponse created = InterfaceStatusMapper.toResponse(
        interfaceStatusService.create(new InterfaceStatusCreateRequest(
            project.getProjectId(), "Draft", "DF", 1, true)));

    InterfaceStatusUpdateRequest updateRequest =
        new InterfaceStatusUpdateRequest("DraftUpdated", "DFU", 2, false);

    InterfaceStatusResponse updated = InterfaceStatusMapper.toResponse(
        interfaceStatusService.update(created.statusId(), updateRequest));

    assertEquals("DraftUpdated", updated.name());
    assertEquals(2, updated.seqOrder());
  }

  @Test
  void getInterfaceStatuses_returnsAll() {
    interfaceStatusService.create(new InterfaceStatusCreateRequest(
        project.getProjectId(), "S1", "S1", 1, true));
    interfaceStatusService.create(new InterfaceStatusCreateRequest(
        project.getProjectId(), "S2", "S2", 2, false));

    assertEquals(2, interfaceStatusService.getAll().size());
  }

  @Test
  void deleteInterfaceStatus_success() {
    InterfaceStatusResponse created = InterfaceStatusMapper.toResponse(
        interfaceStatusService.create(new InterfaceStatusCreateRequest(
            project.getProjectId(), "S3", "S3", 1, true)));

    interfaceStatusService.delete(created.statusId());

    assertEquals(0, interfaceStatusJpaRepository.count());
    assertThrows(EntityNotFoundException.class,
        () -> interfaceStatusService.getById(created.statusId()));
  }
}
