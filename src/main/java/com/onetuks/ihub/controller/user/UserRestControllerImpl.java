package com.onetuks.ihub.controller.user;

import static com.onetuks.ihub.config.RoleDataInitializer.USER_FULL_ACCESS;

import com.onetuks.ihub.annotation.RequiresRole;
import com.onetuks.ihub.dto.user.UserCreateRequest;
import com.onetuks.ihub.dto.user.UserResponse;
import com.onetuks.ihub.dto.user.UserUpdateRequest;
import com.onetuks.ihub.mapper.UserMapper;
import com.onetuks.ihub.service.user.UserService;
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
public class UserRestControllerImpl implements UserRestController {

  private final UserService userService;

  @RequiresRole(USER_FULL_ACCESS)
  @Override
  public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
    UserResponse response = UserMapper.toResponse(userService.create(request));
    return ResponseEntity.created(URI.create("/api/users/" + response.email())).body(response);
  }

  @RequiresRole(USER_FULL_ACCESS)
  @Override
  public ResponseEntity<UserResponse> getUser(@PathVariable String email) {
    return ResponseEntity.ok(UserMapper.toResponse(userService.getById(email)));
  }

  @Override
  public ResponseEntity<List<UserResponse>> getUsers() {
    return ResponseEntity.ok(userService.getAll().stream().map(UserMapper::toResponse).toList());
  }

  @RequiresRole(USER_FULL_ACCESS)
  @Override
  public ResponseEntity<UserResponse> updateUser(
      @PathVariable String email, @Valid @RequestBody UserUpdateRequest request) {
    return ResponseEntity.ok(UserMapper.toResponse(userService.update(email, request)));
  }

  @RequiresRole(USER_FULL_ACCESS)
  @Override
  public ResponseEntity<Void> deleteUser(@PathVariable String email) {
    userService.delete(email);
    return ResponseEntity.noContent().build();
  }
}
