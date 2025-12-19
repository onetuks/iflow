package com.onetuks.ihub.controller.user;

import com.onetuks.ihub.dto.user.UserCreateRequest;
import com.onetuks.ihub.dto.user.UserResponse;
import com.onetuks.ihub.dto.user.UserUpdateRequest;
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
    UserResponse response = userService.create(request);
    return ResponseEntity.created(URI.create("/api/users/" + response.userId())).body(response);
  }

  @Override
  public ResponseEntity<UserResponse> getUser(@PathVariable Long userId) {
    return ResponseEntity.ok(userService.getById(userId));
  }

  @Override
  public ResponseEntity<List<UserResponse>> getUsers() {
    return ResponseEntity.ok(userService.getAll());
  }

  @Override
  public ResponseEntity<UserResponse> updateUser(
      @PathVariable Long userId, @Valid @RequestBody UserUpdateRequest request) {
    return ResponseEntity.ok(userService.update(userId, request));
  }

  @Override
  public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
    userService.delete(userId);
    return ResponseEntity.noContent().build();
  }
}
