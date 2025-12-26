package com.onetuks.ihub.mapper;

import com.onetuks.ihub.dto.project.ProjectCreateRequest;
import com.onetuks.ihub.dto.project.ProjectResponse;
import com.onetuks.ihub.dto.project.ProjectUpdateRequest;
import com.onetuks.ihub.entity.project.Project;
import com.onetuks.ihub.entity.project.ProjectStatus;
import java.time.LocalDateTime;

public final class ProjectMapper {

  private ProjectMapper() {
  }

  public static ProjectResponse toResponse(Project project) {
    return new ProjectResponse(
        project.getProjectId(),
        project.getTitle(),
        project.getDescription(),
        project.getStartDate(),
        project.getEndDate(),
        project.getStatus(),
        project.getCreatedBy() != null ? project.getCreatedBy().getEmail() : null,
        project.getCurrentAdmin() != null ? project.getCurrentAdmin().getEmail() : null,
        project.getCreatedAt(),
        project.getUpdatedAt());
  }

  public static void applyCreate(Project project, ProjectCreateRequest request) {
    LocalDateTime now = LocalDateTime.now();
    project.setProjectId(UUIDProvider.provideUUID(Project.TABLE_NAME));
    project.setTitle(request.title());
    project.setDescription(request.description());
    project.setStartDate(request.startDate());
    project.setEndDate(request.endDate());
    project.setStatus(request.status());
    project.setCreatedAt(now);
    project.setUpdatedAt(now);
  }

  public static void applyUpdate(Project project, ProjectUpdateRequest request) {
    if (request.title() != null) {
      project.setTitle(request.title());
    }
    if (request.description() != null) {
      project.setDescription(request.description());
    }
    if (request.startDate() != null) {
      project.setStartDate(request.startDate());
    }
    if (request.endDate() != null) {
      project.setEndDate(request.endDate());
    }
    ProjectStatus status = request.status();
    if (status != null) {
      project.setStatus(status);
    }
    project.setUpdatedAt(LocalDateTime.now());
  }
}
