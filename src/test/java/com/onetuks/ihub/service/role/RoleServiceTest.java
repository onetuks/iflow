package com.onetuks.ihub.service.role;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.onetuks.ihub.TestcontainersConfiguration;
import com.onetuks.ihub.dto.role.RoleCreateRequest;
import com.onetuks.ihub.dto.role.RoleGrantRequest;
import com.onetuks.ihub.dto.role.RoleResponse;
import com.onetuks.ihub.dto.role.RoleRevokeRequest;
import com.onetuks.ihub.dto.role.RoleUpdateRequest;
import com.onetuks.ihub.entity.role.Role;
import com.onetuks.ihub.entity.user.User;
import com.onetuks.ihub.mapper.RoleMapper;
import com.onetuks.ihub.repository.RoleJpaRepository;
import com.onetuks.ihub.repository.UserJpaRepository;
import com.onetuks.ihub.service.ServiceTestDataFactory;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class RoleServiceTest {

  @Autowired
  private RoleService roleService;

  @Autowired
  private RoleJpaRepository roleJpaRepository;
  @Autowired
  private UserJpaRepository userJpaRepository;

  private User user;
  private List<Role> roles;

  @BeforeEach
  void setup() {
    user = userJpaRepository.save(ServiceTestDataFactory.createUser());
    roles = roleJpaRepository.saveAll(ServiceTestDataFactory.createRoles());
  }

  @Test
  void createRole_success() {
    RoleCreateRequest request = new RoleCreateRequest("Admin", "Administrator role");

    RoleResponse response = RoleMapper.toResponse(roleService.create(request));

    assertNotNull(response.roleId());
    assertEquals("Admin", response.roleName());
    assertEquals("Administrator role", response.description());
  }

  @Test
  void updateRole_success() {
    RoleResponse created = RoleMapper.toResponse(roleService.create(
        new RoleCreateRequest("Member", "Member role")));
    RoleUpdateRequest updateRequest = new RoleUpdateRequest("Member Updated", "Updated desc");

    RoleResponse updated = RoleMapper.toResponse(roleService.update(created.roleId(), updateRequest));

    assertEquals("Member Updated", updated.roleName());
    assertEquals("Updated desc", updated.description());
  }

  @Test
  void getRoles_returnsAll() {
    long expected = roleJpaRepository.count() + 2;
    roleService.create(new RoleCreateRequest("R1", "Role one"));
    roleService.create(new RoleCreateRequest("R2", "Role two"));

    List<RoleResponse> responses = roleService.getAll().stream().map(RoleMapper::toResponse).toList();

    assertEquals(expected, responses.size());
  }

  @Test
  void deleteRole_success() {
    // Given
    long expected = roleJpaRepository.count();
    Role role = roleService.create(new RoleCreateRequest("Temp", "Temp role"));

    roleService.delete(role.getRoleId());

    assertEquals(expected, roleJpaRepository.count());
    assertThrows(EntityNotFoundException.class, () -> roleService.getById(role.getRoleId()));
  }

  @Test
  void grantRole_success() {
    // Given
    RoleGrantRequest request = createRoleGrantRequest();

    // When
    List<Role> results = roleService.grant(request);

    // Then
    assertThat(results).hasSize(request.roleIds().size());
  }

  @Test
  void revokeRole_success() {
    // Given
    roleService.grant(createRoleGrantRequest());
    RoleRevokeRequest request = createRoleRevokeRequest();

    // When
    List<Role> results = roleService.revoke(request);

    // Then
    assertThat(results).hasSize(0);
  }

  private RoleGrantRequest createRoleGrantRequest() {
    return new RoleGrantRequest(user.getEmail(), roles.stream().map(Role::getRoleId).toList());
  }

  private RoleRevokeRequest createRoleRevokeRequest() {
    return new RoleRevokeRequest(user.getEmail(), roles.stream().map(Role::getRoleId).toList());
  }
}
