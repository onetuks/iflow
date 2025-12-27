package com.onetuks.ihub.controller.auth;

import com.onetuks.ihub.dto.auth.AuthResponse;
import com.onetuks.ihub.dto.auth.LoginRequest;
import com.onetuks.ihub.dto.user.UserResponse;
import com.onetuks.ihub.mapper.UserMapper;
import com.onetuks.ihub.security.AuthUserDetails;
import com.onetuks.ihub.security.JwtTokenService;
import com.onetuks.ihub.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthRestControllerImpl implements AuthRestController {

  private final AuthenticationManager authenticationManager;
  private final JwtTokenService jwtTokenService;
  private final UserService userService;

  @Override
  public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.email(), request.password()));

    AuthUserDetails principal = (AuthUserDetails) authentication.getPrincipal();
    JwtTokenService.JwtToken token = jwtTokenService.issueToken(principal);

    AuthResponse response = new AuthResponse(token.value(), "Bearer", token.expiresAt());
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<Void> logout(
      @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {
    String token = resolveToken(authHeader);
    if (token != null) {
      jwtTokenService.revoke(token);
    }
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<UserResponse> me(Authentication authentication) {
    if (!(authentication.getPrincipal() instanceof AuthUserDetails principal)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    return ResponseEntity.ok(UserMapper.toResponse(userService.getById(principal.getUsername())));
  }

  private String resolveToken(String authHeader) {
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return null;
    }
    return authHeader.substring(7);
  }
}
