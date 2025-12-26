package com.onetuks.ihub.dto.role;

import java.util.List;

public record RoleRevokeResponse(
    String email,
    List<String> roleNameOfUser
) {

}
