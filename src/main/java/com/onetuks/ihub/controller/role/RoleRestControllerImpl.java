package com.onetuks.ihub.controller.role;

import com.onetuks.ihub.dto.role.RoleCreateRequest;
import com.onetuks.ihub.dto.role.RoleGrantRequest;
import com.onetuks.ihub.dto.role.RoleGrantResponse;
import com.onetuks.ihub.dto.role.RoleResponse;
import com.onetuks.ihub.dto.role.RoleRevokeRequest;
import com.onetuks.ihub.dto.role.RoleRevokeResponse;
import com.onetuks.ihub.dto.role.RoleUpdateRequest;
import com.onetuks.ihub.mapper.RoleMapper;
import com.onetuks.ihub.service.role.RoleService;
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
public class RoleRestControllerImpl implements RoleRestController {

  private final RoleService roleService;

  @Override
  public ResponseEntity<RoleGrantResponse> grantRoleToUser(@Valid @RequestBody RoleGrantRequest request) {
    RoleGrantResponse response = RoleMapper.toGrantResponse(request.email(), roleService.grant(request));
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<RoleRevokeResponse> revokeRoleFromUser(@Valid @RequestBody RoleRevokeRequest request) {
    RoleRevokeResponse response = RoleMapper.toRevokeResponse(request.email(), roleService.revoke(request));
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<RoleResponse> createRole(@Valid @RequestBody RoleCreateRequest request) {
    RoleResponse response = RoleMapper.toResponse(roleService.create(request));
    return ResponseEntity.created(URI.create("/api/roles/" + response.roleId())).body(response);
  }

  @Override
  public ResponseEntity<RoleResponse> getRole(@PathVariable String roleId) {
    return ResponseEntity.ok(RoleMapper.toResponse(roleService.getById(roleId)));
  }

  @Override
  public ResponseEntity<List<RoleResponse>> getRoles() {
    return ResponseEntity.ok(roleService.getAll().stream().map(RoleMapper::toResponse).toList());
  }

  @Override
  public ResponseEntity<RoleResponse> updateRole(
      @PathVariable String roleId, @Valid @RequestBody RoleUpdateRequest request) {
    return ResponseEntity.ok(RoleMapper.toResponse(roleService.update(roleId, request)));
  }

  @Override
  public ResponseEntity<Void> deleteRole(@PathVariable String roleId) {
    roleService.delete(roleId);
    return ResponseEntity.noContent().build();
  }
}
