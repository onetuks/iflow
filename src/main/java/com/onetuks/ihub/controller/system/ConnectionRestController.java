package com.onetuks.ihub.controller.system;

import com.onetuks.ihub.dto.connection.ConnectionCreateRequest;
import com.onetuks.ihub.dto.connection.ConnectionResponse;
import com.onetuks.ihub.dto.connection.ConnectionUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/connections")
@Tag(name = "Connection", description = "Connection management APIs")
public interface ConnectionRestController {

  @Operation(summary = "Create connection")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Connection created"),
    @ApiResponse(responseCode = "400", description = "Invalid request"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PostMapping
  ResponseEntity<ConnectionResponse> createConnection(
      @Valid @RequestBody ConnectionCreateRequest request);

  @Operation(summary = "Get connection by id")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Connection found"),
    @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
    @ApiResponse(responseCode = "404", description = "Connection not found"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @GetMapping("/{connectionId}")
  ResponseEntity<ConnectionResponse> getConnection(@PathVariable String connectionId);

  @Operation(summary = "List connections")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Connections listed"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @GetMapping
  ResponseEntity<List<ConnectionResponse>> getConnections();

  @Operation(summary = "Update connection")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Connection updated"),
    @ApiResponse(responseCode = "400", description = "Invalid request"),
    @ApiResponse(responseCode = "404", description = "Connection not found"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PutMapping("/{connectionId}")
  ResponseEntity<ConnectionResponse> updateConnection(
      @PathVariable String connectionId,
      @Valid @RequestBody ConnectionUpdateRequest request);

  @Operation(summary = "Delete connection")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Connection deleted"),
    @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
    @ApiResponse(responseCode = "404", description = "Connection not found"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @DeleteMapping("/{connectionId}")
  ResponseEntity<Void> deleteConnection(@PathVariable String connectionId);
}
