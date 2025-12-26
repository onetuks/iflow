package com.onetuks.ihub.dto.role;

import jakarta.validation.constraints.NotBlank;

public record RoleCreateRequest(
    @NotBlank String roleName,
    String description
) {
}
