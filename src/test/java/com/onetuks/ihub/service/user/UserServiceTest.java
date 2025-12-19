package com.onetuks.ihub.service.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.onetuks.ihub.TestcontainersConfiguration;
import com.onetuks.ihub.dto.user.UserCreateRequest;
import com.onetuks.ihub.dto.user.UserResponse;
import com.onetuks.ihub.dto.user.UserUpdateRequest;
import com.onetuks.ihub.entity.user.UserRole;
import com.onetuks.ihub.entity.user.UserStatus;
import com.onetuks.ihub.repository.UserJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
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

  @AfterEach
  void tearDown() {
    userJpaRepository.deleteAll();
  }

  @Test
  void createUser_success() {
    UserCreateRequest request = createRequest("user1@example.com", "User One");

    UserResponse response = userService.create(request);

    assertNotNull(response.userId());
    assertEquals("user1@example.com", response.email());
    assertEquals("User One", response.name());
    assertEquals(UserStatus.ACTIVE, response.status());
    assertNotNull(response.createdAt());
    assertNotNull(response.updatedAt());
  }

  @Test
  void updateUser_success() {
    UserResponse created = userService.create(createRequest("user2@example.com", "User Two"));
    UserUpdateRequest updateRequest = new UserUpdateRequest(
        "user2_new@example.com",
        "newPass",
        "User Two Updated",
        "NewCo",
        "Lead",
        "010-0000-0000",
        "http://example.com/img.png",
        UserStatus.INACTIVE,
        UserRole.LEGACY);

    UserResponse updated = userService.update(created.userId(), updateRequest);

    assertEquals("user2_new@example.com", updated.email());
    assertEquals("User Two Updated", updated.name());
    assertEquals("NewCo", updated.company());
    assertEquals("Lead", updated.position());
    assertEquals("010-0000-0000", updated.phoneNumber());
    assertEquals("http://example.com/img.png", updated.profileImageUrl());
    assertEquals(UserStatus.INACTIVE, updated.status());
    assertEquals(UserRole.LEGACY, updated.role());
  }

  @Test
  void getUsers_returnsAll() {
    userService.create(createRequest("a@example.com", "A"));
    userService.create(createRequest("b@example.com", "B"));

    List<UserResponse> responses = userService.getAll();

    assertEquals(2, responses.size());
  }

  @Test
  void deleteUser_success() {
    UserResponse created = userService.create(createRequest("user3@example.com", "User Three"));

    userService.delete(created.userId());

    assertEquals(0, userJpaRepository.count());
    assertThrows(EntityNotFoundException.class, () -> userService.getById(created.userId()));
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
        UserStatus.ACTIVE,
        UserRole.EAI);
  }
}
