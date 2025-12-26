package com.onetuks.ihub.mapper;

import com.onetuks.ihub.dto.task.TaskFilterGroupCreateRequest;
import com.onetuks.ihub.dto.task.TaskFilterGroupResponse;
import com.onetuks.ihub.dto.task.TaskFilterGroupUpdateRequest;
import com.onetuks.ihub.entity.task.TaskFilterGroup;
import java.time.LocalDateTime;

public final class TaskFilterGroupMapper {

  private TaskFilterGroupMapper() {
  }

  public static TaskFilterGroupResponse toResponse(TaskFilterGroup group) {
    return new TaskFilterGroupResponse(
        group.getGroupId(),
        group.getUser() != null ? group.getUser().getEmail() : null,
        group.getProject() != null ? group.getProject().getProjectId() : null,
        group.getName(),
        group.getAssigneeKeyword(),
        group.getAuthorKeyword(),
        group.getDateType(),
        group.getDateFrom(),
        group.getDateTo(),
        group.getCreatedAt(),
        group.getUpdatedAt(),
        group.getDeletedAt());
  }

  public static void applyCreate(TaskFilterGroup group, TaskFilterGroupCreateRequest request) {
    LocalDateTime now = LocalDateTime.now();
    group.setGroupId(UUIDProvider.provideUUID(TaskFilterGroup.TABLE_NAME));
    group.setName(request.name());
    group.setAssigneeKeyword(request.assigneeKeyword());
    group.setAuthorKeyword(request.authorKeyword());
    group.setDateType(request.dateType());
    group.setDateFrom(request.dateFrom());
    group.setDateTo(request.dateTo());
    group.setCreatedAt(now);
    group.setUpdatedAt(now);
  }

  public static void applyUpdate(TaskFilterGroup group, TaskFilterGroupUpdateRequest request) {
    if (request.name() != null) {
      group.setName(request.name());
    }
    if (request.assigneeKeyword() != null) {
      group.setAssigneeKeyword(request.assigneeKeyword());
    }
    if (request.authorKeyword() != null) {
      group.setAuthorKeyword(request.authorKeyword());
    }
    if (request.dateType() != null) {
      group.setDateType(request.dateType());
    }
    if (request.dateFrom() != null) {
      group.setDateFrom(request.dateFrom());
    }
    if (request.dateTo() != null) {
      group.setDateTo(request.dateTo());
    }
    if (request.deletedAt() != null) {
      group.setDeletedAt(request.deletedAt().atStartOfDay());
    }
    group.setUpdatedAt(LocalDateTime.now());
  }
}
