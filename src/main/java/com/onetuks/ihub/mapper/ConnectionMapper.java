package com.onetuks.ihub.mapper;

import com.onetuks.ihub.dto.connection.ConnectionCreateRequest;
import com.onetuks.ihub.dto.connection.ConnectionResponse;
import com.onetuks.ihub.dto.connection.ConnectionUpdateRequest;
import com.onetuks.ihub.entity.system.Connection;
import java.time.LocalDateTime;

public final class ConnectionMapper {

  private ConnectionMapper() {
  }

  public static ConnectionResponse toResponse(Connection connection) {
    return new ConnectionResponse(
        connection.getConnectionId(),
        connection.getProject() != null ? connection.getProject().getProjectId() : null,
        connection.getSystem() != null ? connection.getSystem().getSystemId() : null,
        connection.getName(),
        connection.getProtocol(),
        connection.getHost(),
        connection.getPort(),
        connection.getPath(),
        connection.getUsername(),
        connection.getAuthType(),
        connection.getExtraConfig(),
        connection.getStatus(),
        connection.getDescription(),
        connection.getCreatedBy() != null ? connection.getCreatedBy().getEmail() : null,
        connection.getUpdatedBy() != null ? connection.getUpdatedBy().getEmail() : null,
        connection.getCreatedAt(),
        connection.getUpdatedAt());
  }

  public static void applyCreate(Connection connection, ConnectionCreateRequest request) {
    LocalDateTime now = LocalDateTime.now();
    connection.setConnectionId(UUIDProvider.provideUUID(Connection.TABLE_NAME));
    connection.setName(request.name());
    connection.setProtocol(request.protocol());
    connection.setHost(request.host());
    connection.setPort(request.port());
    connection.setPath(request.path());
    connection.setUsername(request.username());
    connection.setAuthType(request.authType());
    connection.setExtraConfig(request.extraConfig());
    connection.setStatus(request.status());
    connection.setDescription(request.description());
    connection.setCreatedAt(now);
    connection.setUpdatedAt(now);
  }

  public static void applyUpdate(Connection connection, ConnectionUpdateRequest request) {
    if (request.name() != null) {
      connection.setName(request.name());
    }
    if (request.protocol() != null) {
      connection.setProtocol(request.protocol());
    }
    if (request.host() != null) {
      connection.setHost(request.host());
    }
    if (request.port() != null) {
      connection.setPort(request.port());
    }
    if (request.path() != null) {
      connection.setPath(request.path());
    }
    if (request.username() != null) {
      connection.setUsername(request.username());
    }
    if (request.authType() != null) {
      connection.setAuthType(request.authType());
    }
    if (request.extraConfig() != null) {
      connection.setExtraConfig(request.extraConfig());
    }
    if (request.status() != null) {
      connection.setStatus(request.status());
    }
    if (request.description() != null) {
      connection.setDescription(request.description());
    }
    connection.setUpdatedAt(LocalDateTime.now());
  }
}
