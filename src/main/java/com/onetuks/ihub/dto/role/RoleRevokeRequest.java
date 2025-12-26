package com.onetuks.ihub.dto.role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record RoleRevokeRequest(
    @Email String email,
    @NotBlank List<String> roleIds
) {

}
