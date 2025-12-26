package com.onetuks.ihub.dto.connection;

import com.onetuks.ihub.entity.system.ConnectionStatus;
import com.onetuks.ihub.entity.system.Protocol;

public record ConnectionUpdateRequest(
    String projectId,
    String systemId,
    String name,
    Protocol protocol,
    String host,
    Integer port,
    String path,
    String username,
    String authType,
    String extraConfig,
    ConnectionStatus status,
    String description,
    String updatedById
) {
}
