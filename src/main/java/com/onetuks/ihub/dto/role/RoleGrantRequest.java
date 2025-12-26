package com.onetuks.ihub.dto.role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record RoleGrantRequest(
    @Email String email,
    @NotEmpty List<String> roleIds
) {

}
