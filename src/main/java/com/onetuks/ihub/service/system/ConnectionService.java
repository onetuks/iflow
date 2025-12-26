package com.onetuks.ihub.service.system;

import com.onetuks.ihub.dto.connection.ConnectionCreateRequest;
import com.onetuks.ihub.dto.connection.ConnectionUpdateRequest;
import com.onetuks.ihub.entity.system.Connection;
import com.onetuks.ihub.entity.project.Project;
import com.onetuks.ihub.entity.system.System;
import com.onetuks.ihub.entity.user.User;
import com.onetuks.ihub.mapper.ConnectionMapper;
import com.onetuks.ihub.repository.ConnectionJpaRepository;
import com.onetuks.ihub.repository.ProjectJpaRepository;
import com.onetuks.ihub.repository.SystemJpaRepository;
import com.onetuks.ihub.repository.UserJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConnectionService {

  private final ConnectionJpaRepository connectionJpaRepository;
  private final ProjectJpaRepository projectJpaRepository;
  private final SystemJpaRepository systemJpaRepository;
  private final UserJpaRepository userJpaRepository;

  @Transactional
  public Connection create(ConnectionCreateRequest request) {
    Connection connection = new Connection();
    ConnectionMapper.applyCreate(connection, request);
    connection.setProject(findProject(request.projectId()));
    connection.setSystem(findSystem(request.systemId()));
    connection.setCreatedBy(findUser(request.createdById()));
    connection.setUpdatedBy(findUser(request.updatedById()));
    return connectionJpaRepository.save(connection);
  }

  @Transactional(readOnly = true)
  public Connection getById(String connectionId) {
    return findEntity(connectionId);
  }

  @Transactional(readOnly = true)
  public List<Connection> getAll() {
    return connectionJpaRepository.findAll();
  }

  @Transactional
  public Connection update(String connectionId, ConnectionUpdateRequest request) {
    Connection connection = findEntity(connectionId);
    ConnectionMapper.applyUpdate(connection, request);
    if (request.projectId() != null) {
      connection.setProject(findProject(request.projectId()));
    }
    if (request.systemId() != null) {
      connection.setSystem(findSystem(request.systemId()));
    }
    if (request.updatedById() != null) {
      connection.setUpdatedBy(findUser(request.updatedById()));
    }
    return connection;
  }

  @Transactional
  public void delete(String connectionId) {
    Connection connection = findEntity(connectionId);
    connectionJpaRepository.delete(connection);
  }

  private Connection findEntity(String connectionId) {
    return connectionJpaRepository.findById(connectionId)
        .orElseThrow(() -> new EntityNotFoundException("Connection not found: " + connectionId));
  }

  private Project findProject(String projectId) {
    return projectJpaRepository.findById(projectId)
        .orElseThrow(() -> new EntityNotFoundException("Project not found: " + projectId));
  }

  private System findSystem(String systemId) {
    return systemJpaRepository.findById(systemId)
        .orElseThrow(() -> new EntityNotFoundException("System not found: " + systemId));
  }

  private User findUser(String userId) {
    return userJpaRepository.findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));
  }
}
