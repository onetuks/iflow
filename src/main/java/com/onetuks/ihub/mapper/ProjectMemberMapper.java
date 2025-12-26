package com.onetuks.ihub.mapper;

import com.onetuks.ihub.dto.project.ProjectMemberCreateRequest;
import com.onetuks.ihub.dto.project.ProjectMemberResponse;
import com.onetuks.ihub.dto.project.ProjectMemberUpdateRequest;
import com.onetuks.ihub.entity.project.ProjectMember;

public final class ProjectMemberMapper {

  private ProjectMemberMapper() {
  }

  public static ProjectMemberResponse toResponse(ProjectMember projectMember) {
    return new ProjectMemberResponse(
        projectMember.getProjectMemberId(),
        projectMember.getProject() != null ? projectMember.getProject().getProjectId() : null,
        projectMember.getUser() != null ? projectMember.getUser().getEmail() : null,
        projectMember.getRole(),
        projectMember.getJoinedAt(),
        projectMember.getLeftAt());
  }

  public static void applyCreate(ProjectMember projectMember, ProjectMemberCreateRequest request) {
    projectMember.setProjectMemberId(UUIDProvider.provideUUID(ProjectMember.TABLE_NAME));
    projectMember.setRole(request.role());
    projectMember.setJoinedAt(request.joinedAt());
    projectMember.setLeftAt(request.leftAt());
  }

  public static void applyUpdate(ProjectMember projectMember, ProjectMemberUpdateRequest request) {
    if (request.role() != null) {
      projectMember.setRole(request.role());
    }
    if (request.leftAt() != null) {
      projectMember.setLeftAt(request.leftAt());
    }
  }
}
