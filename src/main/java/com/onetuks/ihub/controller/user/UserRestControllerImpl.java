package com.onetuks.ihub.controller.user;

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

  @Override
  public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
    UserResponse response = UserMapper.toResponse(userService.create(request));
    return ResponseEntity.created(URI.create("/api/users/" + response.email())).body(response);
  }

  @Override
  public ResponseEntity<UserResponse> getUser(@PathVariable String userId) {
    return ResponseEntity.ok(UserMapper.toResponse(userService.getById(userId)));
  }

  @Override
  public ResponseEntity<List<UserResponse>> getUsers() {
    return ResponseEntity.ok(userService.getAll().stream().map(UserMapper::toResponse).toList());
  }

  @Override
  public ResponseEntity<UserResponse> updateUser(
      @PathVariable String userId, @Valid @RequestBody UserUpdateRequest request) {
    return ResponseEntity.ok(UserMapper.toResponse(userService.update(userId, request)));
  }

  @Override
  public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
    userService.delete(userId);
    return ResponseEntity.noContent().build();
  }
}
