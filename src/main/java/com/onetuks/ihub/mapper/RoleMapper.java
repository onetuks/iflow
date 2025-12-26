package com.onetuks.ihub.mapper;

import com.onetuks.ihub.dto.role.RoleCreateRequest;
import com.onetuks.ihub.dto.role.RoleGrantResponse;
import com.onetuks.ihub.dto.role.RoleResponse;
import com.onetuks.ihub.dto.role.RoleRevokeResponse;
import com.onetuks.ihub.dto.role.RoleUpdateRequest;
import com.onetuks.ihub.entity.role.Role;
import jakarta.validation.constraints.Email;
import java.util.List;

public final class RoleMapper {

  private RoleMapper() {
  }

  public static RoleResponse toResponse(Role role) {
    return new RoleResponse(role.getRoleId(), role.getRoleName(), role.getDescription());
  }

  public static void applyCreate(Role role, RoleCreateRequest request) {
    role.setRoleId(UUIDProvider.provideUUID(Role.TABLE_NAME));
    role.setRoleName(request.roleName());
    role.setDescription(request.description());
  }

  public static void applyUpdate(Role role, RoleUpdateRequest request) {
    if (request.roleName() != null) {
      role.setRoleName(request.roleName());
    }
    if (request.description() != null) {
      role.setDescription(request.description());
    }
  }

  public static RoleGrantResponse toGrantResponse(String email, List<Role> roles) {
    return new RoleGrantResponse(email, roles.stream().map(Role::getRoleName).toList());
  }

  public static RoleRevokeResponse toRevokeResponse(String email, List<Role> revoke) {
    return new RoleRevokeResponse(email, revoke.stream().map(Role::getRoleName).toList());
  }
}
