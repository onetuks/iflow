package com.onetuks.ihub.service.user;

import com.onetuks.ihub.dto.user.UserCreateRequest;
import com.onetuks.ihub.dto.user.UserUpdateRequest;
import com.onetuks.ihub.entity.user.User;
import com.onetuks.ihub.entity.user.UserStatus;
import com.onetuks.ihub.mapper.UserMapper;
import com.onetuks.ihub.repository.UserJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserJpaRepository userJpaRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public User create(UserCreateRequest request) {
    User newUser = new User();
    UserMapper.applyCreate(newUser, request);
    newUser.setPassword(passwordEncoder.encode(request.password()));
    return userJpaRepository.save(newUser);
  }

  @Transactional(readOnly = true)
  public User getById(String email) {
    return findEntity(email);
  }

  @Transactional(readOnly = true)
  public List<User> getAll() {
    return userJpaRepository.findAll();
  }

  @Transactional
  public User update(String email, UserUpdateRequest request) {
    User target = findEntity(email);
    if (request.password() != null) {
      target.setPassword(passwordEncoder.encode(request.password()));
    }
    UserMapper.applyUpdate(target, request);
    return target;
  }

  @Transactional
  public User delete(String email) {
    User target = findEntity(email);
    target.setStatus(UserStatus.DELETED);
    return target;
  }

  private User findEntity(String email) {
    return userJpaRepository.findById(email)
        .orElseThrow(() -> new EntityNotFoundException("User not found: " + email));
  }
}
