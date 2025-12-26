package com.onetuks.ihub.dto.connection;

import com.onetuks.ihub.entity.system.ConnectionStatus;
import com.onetuks.ihub.entity.system.Protocol;
import jakarta.validation.constraints.NotNull;

public record ConnectionCreateRequest(
    @NotNull String projectId,
    @NotNull String systemId,
    String name,
    Protocol protocol,
    String host,
    Integer port,
    String path,
    String username,
    String authType,
    String extraConfig,
    @NotNull ConnectionStatus status,
    String description,
    @NotNull String createdById,
    @NotNull String updatedById
) {
}
