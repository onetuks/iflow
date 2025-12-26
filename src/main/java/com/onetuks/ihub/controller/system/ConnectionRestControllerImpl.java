package com.onetuks.ihub.controller.system;

import com.onetuks.ihub.dto.connection.ConnectionCreateRequest;
import com.onetuks.ihub.dto.connection.ConnectionResponse;
import com.onetuks.ihub.dto.connection.ConnectionUpdateRequest;
import com.onetuks.ihub.mapper.ConnectionMapper;
import com.onetuks.ihub.service.system.ConnectionService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ConnectionRestControllerImpl implements ConnectionRestController {

  private final ConnectionService connectionService;

  @Override
  public ResponseEntity<ConnectionResponse> createConnection(
      @Valid @RequestBody ConnectionCreateRequest request) {
    ConnectionResponse response = ConnectionMapper.toResponse(connectionService.create(request));
    return ResponseEntity.created(URI.create("/api/connections/" + response.connectionId()))
        .body(response);
  }

  @Override
  public ResponseEntity<ConnectionResponse> getConnection(@PathVariable String connectionId) {
    return ResponseEntity.ok(ConnectionMapper.toResponse(connectionService.getById(connectionId)));
  }

  @Override
  public ResponseEntity<List<ConnectionResponse>> getConnections() {
    return ResponseEntity.ok(
        connectionService.getAll().stream().map(ConnectionMapper::toResponse).toList());
  }

  @Override
  public ResponseEntity<ConnectionResponse> updateConnection(
      @PathVariable String connectionId, @Valid @RequestBody ConnectionUpdateRequest request) {
    return ResponseEntity.ok(
        ConnectionMapper.toResponse(connectionService.update(connectionId, request)));
  }

  @Override
  public ResponseEntity<Void> deleteConnection(@PathVariable String connectionId) {
    connectionService.delete(connectionId);
    return ResponseEntity.noContent().build();
  }
}
