package com.onetuks.ihub.security;

import com.onetuks.ihub.entity.user.User;
import com.onetuks.ihub.entity.user.UserStatus;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthUserDetails implements UserDetails {

  private final String email;
  private final String password;
  private final UserStatus status;
  private final List<String> roles;

  private AuthUserDetails(String email, String password, UserStatus status, List<String> roles) {
    this.email = email;
    this.password = password;
    this.status = status;
    this.roles = List.copyOf(roles);
  }

  public static AuthUserDetails from(User user, List<String> roles) {
    return new AuthUserDetails(user.getEmail(), user.getPassword(), user.getStatus(), roles);
  }

  public List<String> getRoles() {
    return roles;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles.stream().map(SimpleGrantedAuthority::new).toList();
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return status != UserStatus.LOCKED;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return status != UserStatus.DELETED && status != UserStatus.INACTIVE;
  }
}
