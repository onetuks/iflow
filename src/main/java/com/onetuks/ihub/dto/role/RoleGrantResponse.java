package com.onetuks.ihub.dto.role;

import java.util.List;

public record RoleGrantResponse(
    String email,
    List<String> roleNameOfUser
) {

}
