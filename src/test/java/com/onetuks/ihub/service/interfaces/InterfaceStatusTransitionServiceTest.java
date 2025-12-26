package com.onetuks.ihub.service.interfaces;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.onetuks.ihub.TestcontainersConfiguration;
import com.onetuks.ihub.dto.interfaces.InterfaceStatusTransitionCreateRequest;
import com.onetuks.ihub.dto.interfaces.InterfaceStatusTransitionResponse;
import com.onetuks.ihub.dto.interfaces.InterfaceStatusTransitionUpdateRequest;
import com.onetuks.ihub.entity.interfaces.InterfaceRole;
import com.onetuks.ihub.entity.interfaces.InterfaceStatus;
import com.onetuks.ihub.entity.interfaces.InterfaceStatusTransitionStatus;
import com.onetuks.ihub.entity.project.Project;
import com.onetuks.ihub.entity.user.User;
import com.onetuks.ihub.mapper.InterfaceStatusTransitionMapper;
import com.onetuks.ihub.repository.InterfaceStatusJpaRepository;
import com.onetuks.ihub.repository.InterfaceStatusTransitionJpaRepository;
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
class InterfaceStatusTransitionServiceTest {

  @Autowired
  private InterfaceStatusTransitionService interfaceStatusTransitionService;

  @Autowired
  private InterfaceStatusTransitionJpaRepository interfaceStatusTransitionJpaRepository;

  @Autowired
  private InterfaceStatusJpaRepository interfaceStatusJpaRepository;

  @Autowired
  private ProjectJpaRepository projectJpaRepository;

  @Autowired
  private UserJpaRepository userJpaRepository;

  private Project project;
  private User user;
  private InterfaceStatus fromStatus;
  private InterfaceStatus toStatus;

  @BeforeEach
  void setUp() {
    user = ServiceTestDataFactory.createUser(userJpaRepository);
    project = ServiceTestDataFactory.createProject(projectJpaRepository, user, user, "IFTranProj");
    fromStatus =
        ServiceTestDataFactory.createInterfaceStatus(interfaceStatusJpaRepository, project, "Draft", 1);
    toStatus =
        ServiceTestDataFactory.createInterfaceStatus(interfaceStatusJpaRepository, project, "Live", 2);
  }

  @AfterEach
  void tearDown() {
    interfaceStatusTransitionJpaRepository.deleteAll();
    interfaceStatusJpaRepository.deleteAll();
    projectJpaRepository.deleteAll();
    userJpaRepository.deleteAll();
  }

  @Test
  void createInterfaceStatusTransition_success() {
    InterfaceStatusTransitionCreateRequest request = new InterfaceStatusTransitionCreateRequest(
        project.getProjectId(),
        fromStatus.getStatusId(),
        toStatus.getStatusId(),
        InterfaceRole.ADMIN,
        InterfaceStatusTransitionStatus.ACTIVE,
        user.getEmail());

    InterfaceStatusTransitionResponse response = InterfaceStatusTransitionMapper.toResponse(
        interfaceStatusTransitionService.create(request));

    assertNotNull(response.transitionId());
    assertEquals(InterfaceRole.ADMIN, response.allowedRole());
    assertEquals(InterfaceStatusTransitionStatus.ACTIVE, response.status());
  }

  @Test
  void updateInterfaceStatusTransition_success() {
    InterfaceStatusTransitionResponse created = InterfaceStatusTransitionMapper.toResponse(
        interfaceStatusTransitionService.create(
        new InterfaceStatusTransitionCreateRequest(
            project.getProjectId(), fromStatus.getStatusId(), toStatus.getStatusId(),
            InterfaceRole.ADMIN, InterfaceStatusTransitionStatus.ACTIVE, user.getEmail())));

    InterfaceStatusTransitionUpdateRequest updateRequest =
        new InterfaceStatusTransitionUpdateRequest(
            fromStatus.getStatusId(),
            toStatus.getStatusId(),
            InterfaceRole.MEMBER,
            InterfaceStatusTransitionStatus.INACTIVE);

    InterfaceStatusTransitionResponse updated = InterfaceStatusTransitionMapper.toResponse(
        interfaceStatusTransitionService.update(created.transitionId(), updateRequest));

    assertEquals(InterfaceRole.MEMBER, updated.allowedRole());
    assertEquals(InterfaceStatusTransitionStatus.INACTIVE, updated.status());
  }

  @Test
  void getInterfaceStatusTransitions_returnsAll() {
    interfaceStatusTransitionService.create(new InterfaceStatusTransitionCreateRequest(
        project.getProjectId(), fromStatus.getStatusId(), toStatus.getStatusId(),
        InterfaceRole.ADMIN, InterfaceStatusTransitionStatus.ACTIVE, user.getEmail()));
    interfaceStatusTransitionService.create(new InterfaceStatusTransitionCreateRequest(
        project.getProjectId(), toStatus.getStatusId(), fromStatus.getStatusId(),
        InterfaceRole.MEMBER, InterfaceStatusTransitionStatus.ACTIVE, user.getEmail()));

    assertEquals(2, interfaceStatusTransitionService.getAll().size());
  }

  @Test
  void deleteInterfaceStatusTransition_success() {
    InterfaceStatusTransitionResponse created = InterfaceStatusTransitionMapper.toResponse(
        interfaceStatusTransitionService.create(
        new InterfaceStatusTransitionCreateRequest(
            project.getProjectId(), fromStatus.getStatusId(), toStatus.getStatusId(),
            InterfaceRole.ADMIN, InterfaceStatusTransitionStatus.ACTIVE, user.getEmail())));

    interfaceStatusTransitionService.delete(created.transitionId());

    assertEquals(0, interfaceStatusTransitionJpaRepository.count());
    assertThrows(EntityNotFoundException.class,
        () -> interfaceStatusTransitionService.getById(created.transitionId()));
  }
}
