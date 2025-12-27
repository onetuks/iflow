package com.onetuks.ihub.security;

import com.onetuks.ihub.entity.user.User;
import com.onetuks.ihub.repository.UserJpaRepository;
import com.onetuks.ihub.repository.UserRoleJpaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthUserDetailsService implements UserDetailsService {

  private final UserJpaRepository userJpaRepository;
  private final UserRoleJpaRepository userRoleJpaRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userJpaRepository.findById(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

    List<String> roles = userRoleJpaRepository.findAllByUserEmail(username).stream()
        .map(userRole -> userRole.getRole().getRoleName())
        .toList();

    return AuthUserDetails.from(user, roles);
  }
}
