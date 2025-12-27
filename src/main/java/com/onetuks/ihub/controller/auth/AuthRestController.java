package com.onetuks.ihub.controller.auth;

import com.onetuks.ihub.dto.auth.AuthResponse;
import com.onetuks.ihub.dto.auth.LoginRequest;
import com.onetuks.ihub.dto.user.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(path = "/api/auth")
@Tag(name = "Auth", description = "Auth management APIs")
public interface AuthRestController {

  @Operation(summary = "Log in")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Logged in"),
      @ApiResponse(responseCode = "400", description = "Invalid request"),
      @ApiResponse(responseCode = "401", description = "No Authorization"),
      @ApiResponse(responseCode = "403", description = "Forbidden"),
      @ApiResponse(responseCode = "404", description = "User not found"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PostMapping(path = "/login")
  ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request);

  @Operation(summary = "Log out")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Logged out"),
      @ApiResponse(responseCode = "400", description = "Invalid request"),
      @ApiResponse(responseCode = "401", description = "No Authorization"),
      @ApiResponse(responseCode = "403", description = "Forbidden"),
      @ApiResponse(responseCode = "404", description = "User not found"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PostMapping(path = "/logout")
  ResponseEntity<Void> logout(
      @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader);

  @Operation(summary = "My Auth")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Show My Auth Info"),
      @ApiResponse(responseCode = "400", description = "Invalid request"),
      @ApiResponse(responseCode = "401", description = "No Authorization"),
      @ApiResponse(responseCode = "403", description = "Forbidden"),
      @ApiResponse(responseCode = "404", description = "User not found"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @GetMapping("/me")
  ResponseEntity<UserResponse> me(Authentication authentication);
}
