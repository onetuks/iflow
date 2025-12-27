package com.onetuks.ihub.service.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.onetuks.ihub.IHubApplicationTests;
import com.onetuks.ihub.TestcontainersConfiguration;
import com.onetuks.ihub.dto.user.UserCreateRequest;
import com.onetuks.ihub.dto.user.UserResponse;
import com.onetuks.ihub.dto.user.UserUpdateRequest;
import com.onetuks.ihub.entity.user.User;
import com.onetuks.ihub.entity.user.UserStatus;
import com.onetuks.ihub.mapper.UserMapper;
import com.onetuks.ihub.repository.UserJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class UserServiceTest {

  @Autowired
  private UserService userService;

  @Autowired
  private UserJpaRepository userJpaRepository;

  @Test
  void createUser_success() {
    UserCreateRequest request = createRequest("user1@example.com", "User One");

    UserResponse response = UserMapper.toResponse(userService.create(request));

    assertNotNull(response.email());
    assertEquals("user1@example.com", response.email());
    assertEquals("User One", response.name());
    assertEquals(UserStatus.ACTIVE, response.status());
    assertNotNull(response.createdAt());
    assertNotNull(response.updatedAt());
  }

  @Test
  void updateUser_success() {
    UserResponse created = UserMapper.toResponse(
        userService.create(createRequest("user2@example.com", "User Two")));
    UserUpdateRequest updateRequest = new UserUpdateRequest(
        "newPass",
        "User Two Updated",
        "NewCo",
        "Lead",
        "010-0000-0000",
        "http://example.com/img.png",
        UserStatus.INACTIVE);

    UserResponse updated = UserMapper.toResponse(
        userService.update(created.email(), updateRequest));

    assertEquals("user2@example.com", updated.email());
    assertEquals("User Two Updated", updated.name());
    assertEquals("NewCo", updated.company());
    assertEquals("Lead", updated.position());
    assertEquals("010-0000-0000", updated.phoneNumber());
    assertEquals("http://example.com/img.png", updated.profileImageUrl());
    assertEquals(UserStatus.INACTIVE, updated.status());
  }

  @Test
  void getUsers_returnsAll() {
    long expected = userJpaRepository.count() + 2;
    userService.create(createRequest("a@example.com", "A"));
    userService.create(createRequest("b@example.com", "B"));

    List<UserResponse> responses = userService.getAll().stream().map(UserMapper::toResponse)
        .toList();

    assertEquals(expected, responses.size());
  }

  @Test
  void deleteUser_success() {
    UserResponse created = UserMapper.toResponse(userService.create(
        createRequest("user3@example.com", "User Three")));

    User result = userService.delete(created.email());

    assertThat(result.getEmail()).isEqualTo(created.email());
    assertThat(result.getStatus()).isEqualTo(UserStatus.DELETED);
  }

  private UserCreateRequest createRequest(String email, String name) {
    return new UserCreateRequest(
        email,
        "password",
        name,
        "Company",
        "Position",
        "010-1234-5678",
        "http://example.com/profile.png",
        UserStatus.ACTIVE);
  }
}
