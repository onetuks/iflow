package com.onetuks.ihub.service.connection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.onetuks.ihub.TestcontainersConfiguration;
import com.onetuks.ihub.dto.connection.ConnectionCreateRequest;
import com.onetuks.ihub.dto.connection.ConnectionResponse;
import com.onetuks.ihub.dto.connection.ConnectionUpdateRequest;
import com.onetuks.ihub.entity.system.ConnectionStatus;
import com.onetuks.ihub.entity.system.Protocol;
import com.onetuks.ihub.entity.project.Project;
import com.onetuks.ihub.entity.user.User;
import com.onetuks.ihub.mapper.ConnectionMapper;
import com.onetuks.ihub.repository.ConnectionJpaRepository;
import com.onetuks.ihub.repository.ProjectJpaRepository;
import com.onetuks.ihub.repository.SystemJpaRepository;
import com.onetuks.ihub.repository.UserJpaRepository;
import com.onetuks.ihub.service.ServiceTestDataFactory;
import com.onetuks.ihub.service.system.ConnectionService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class ConnectionServiceTest {

  @Autowired
  private ConnectionService connectionService;

  @Autowired
  private ConnectionJpaRepository connectionJpaRepository;

  @Autowired
  private ProjectJpaRepository projectJpaRepository;

  @Autowired
  private SystemJpaRepository systemJpaRepository;

  @Autowired
  private UserJpaRepository userJpaRepository;

  private User creator;
  private User updater;
  private Project project;
  private com.onetuks.ihub.entity.system.System system;

  @BeforeEach
  void setUp() {
    creator = ServiceTestDataFactory.createUser(userJpaRepository);
    updater = ServiceTestDataFactory.createUser(userJpaRepository);
    project = ServiceTestDataFactory.createProject(projectJpaRepository, creator, updater, "ConnProj");
    system = ServiceTestDataFactory.createSystem(
        systemJpaRepository, project, creator, updater, "SYS-CONN");
  }

  @AfterEach
  void tearDown() {
    connectionJpaRepository.deleteAll();
    systemJpaRepository.deleteAll();
    projectJpaRepository.deleteAll();
    userJpaRepository.deleteAll();
  }

  @Test
  void createConnection_success() {
    ConnectionCreateRequest request = new ConnectionCreateRequest(
        project.getProjectId(),
        system.getSystemId(),
        "Conn1",
        Protocol.HTTP,
        "localhost",
        8080,
        "/path",
        "user",
        "BASIC",
        "{\"k\":\"v\"}",
        ConnectionStatus.ACTIVE,
        "desc",
        creator.getEmail(),
        updater.getEmail());

    ConnectionResponse response = ConnectionMapper.toResponse(connectionService.create(request));

    assertNotNull(response.connectionId());
    assertEquals("Conn1", response.name());
    assertEquals(Protocol.HTTP, response.protocol());
    assertEquals(ConnectionStatus.ACTIVE, response.status());
  }

  @Test
  void updateConnection_success() {
    ConnectionResponse created = ConnectionMapper.toResponse(connectionService.create(
        new ConnectionCreateRequest(
            project.getProjectId(),
            system.getSystemId(),
            "Conn2",
            Protocol.HTTP,
            "localhost",
            8080,
            "/path",
            "user",
            "BASIC",
            null,
            ConnectionStatus.ACTIVE,
            "desc",
            creator.getEmail(),
            updater.getEmail())));

    ConnectionUpdateRequest updateRequest = new ConnectionUpdateRequest(
        project.getProjectId(),
        system.getSystemId(),
        "Conn2-Updated",
        Protocol.SFTP,
        "example.com",
        22,
        "/updated",
        "updatedUser",
        "KEY",
        "{\"new\":\"json\"}",
        ConnectionStatus.INACTIVE,
        "new desc",
        creator.getEmail());

    ConnectionResponse updated = ConnectionMapper.toResponse(
        connectionService.update(created.connectionId(), updateRequest));

    assertEquals("Conn2-Updated", updated.name());
    assertEquals(Protocol.SFTP, updated.protocol());
    assertEquals("example.com", updated.host());
    assertEquals(ConnectionStatus.INACTIVE, updated.status());
  }

  @Test
  void getConnections_returnsAll() {
    connectionService.create(new ConnectionCreateRequest(
        project.getProjectId(), system.getSystemId(), "C1", Protocol.HTTP, null, null, null,
        null, null, null, ConnectionStatus.ACTIVE, null, creator.getEmail(), updater.getEmail()));
    connectionService.create(new ConnectionCreateRequest(
        project.getProjectId(), system.getSystemId(), "C2", Protocol.HTTP, null, null, null,
        null, null, null, ConnectionStatus.ACTIVE, null, creator.getEmail(), updater.getEmail()));

    assertEquals(2, connectionService.getAll().size());
  }

  @Test
  void deleteConnection_success() {
    ConnectionResponse created = ConnectionMapper.toResponse(connectionService.create(
        new ConnectionCreateRequest(
            project.getProjectId(), system.getSystemId(), "C3", Protocol.HTTP, null, null, null,
            null, null, null, ConnectionStatus.ACTIVE, null, creator.getEmail(), updater.getEmail())));

    connectionService.delete(created.connectionId());

    assertEquals(0, connectionJpaRepository.count());
    assertThrows(EntityNotFoundException.class,
        () -> connectionService.getById(created.connectionId()));
  }
}
