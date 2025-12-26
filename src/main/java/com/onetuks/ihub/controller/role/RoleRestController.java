package com.onetuks.ihub.controller.role;

import com.onetuks.ihub.dto.role.RoleCreateRequest;
import com.onetuks.ihub.dto.role.RoleGrantRequest;
import com.onetuks.ihub.dto.role.RoleGrantResponse;
import com.onetuks.ihub.dto.role.RoleResponse;
import com.onetuks.ihub.dto.role.RoleRevokeRequest;
import com.onetuks.ihub.dto.role.RoleRevokeResponse;
import com.onetuks.ihub.dto.role.RoleUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(path = "/api/roles")
@Tag(name = "Role", description = "Role management APIs")
public interface RoleRestController {

  @Operation(summary = "Grant role to user")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Role granted"),
      @ApiResponse(responseCode = "400", description = "Invalid request"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PostMapping(path = "/grant")
  ResponseEntity<RoleGrantResponse> grantRoleToUser(@Valid @RequestBody RoleGrantRequest request);

  @Operation(summary = "Revoke role from user")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Role revoked"),
      @ApiResponse(responseCode = "400", description = "Invalid request"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PatchMapping(path = "/revoke")
  ResponseEntity<RoleRevokeResponse> revokeRoleFromUser(@Valid @RequestBody RoleRevokeRequest request);

  @Operation(summary = "Create role")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Role created"),
      @ApiResponse(responseCode = "400", description = "Invalid request"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PostMapping
  ResponseEntity<RoleResponse> createRole(@Valid @RequestBody RoleCreateRequest request);

  @Operation(summary = "Get role by id")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Role found"),
      @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
      @ApiResponse(responseCode = "404", description = "Role not found"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @GetMapping("/{roleId}")
  ResponseEntity<RoleResponse> getRole(@PathVariable String roleId);

  @Operation(summary = "List roles")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Roles listed"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @GetMapping
  ResponseEntity<List<RoleResponse>> getRoles();

  @Operation(summary = "Update role")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Role updated"),
      @ApiResponse(responseCode = "400", description = "Invalid request"),
      @ApiResponse(responseCode = "404", description = "Role not found"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PutMapping("/{roleId}")
  ResponseEntity<RoleResponse> updateRole(
      @PathVariable String roleId,
      @Valid @RequestBody RoleUpdateRequest request);

  @Operation(summary = "Delete role")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Role deleted"),
      @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
      @ApiResponse(responseCode = "404", description = "Role not found"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @DeleteMapping("/{roleId}")
  ResponseEntity<Void> deleteRole(@PathVariable String roleId);
}
