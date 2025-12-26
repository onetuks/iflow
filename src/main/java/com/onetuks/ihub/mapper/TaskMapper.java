package com.onetuks.ihub.mapper;

import com.onetuks.ihub.dto.task.TaskCreateRequest;
import com.onetuks.ihub.dto.task.TaskResponse;
import com.onetuks.ihub.dto.task.TaskUpdateRequest;
import com.onetuks.ihub.entity.task.Task;
import java.time.LocalDateTime;

public final class TaskMapper {

  private TaskMapper() {
  }

  public static TaskResponse toResponse(Task task) {
    return new TaskResponse(
        task.getTaskId(),
        task.getProject() != null ? task.getProject().getProjectId() : null,
        task.getParentTask() != null ? task.getParentTask().getTaskId() : null,
        task.getTaskType(),
        task.getAnInterface() != null ? task.getAnInterface().getInterfaceId() : null,
        task.getTitle(),
        task.getDescription(),
        task.getStatus(),
        task.getAssignee() != null ? task.getAssignee().getEmail() : null,
        task.getRequester() != null ? task.getRequester().getEmail() : null,
        task.getStartDate(),
        task.getDueDate(),
        task.getPriority(),
        task.getProgress(),
        task.getCreatedBy() != null ? task.getCreatedBy().getEmail() : null,
        task.getCreatedAt(),
        task.getUpdatedAt());
  }

  public static void applyCreate(Task task, TaskCreateRequest request) {
    LocalDateTime now = LocalDateTime.now();
    task.setTaskId(UUIDProvider.provideUUID(Task.TABLE_NAME));
    task.setTaskType(request.taskType());
    task.setTitle(request.title());
    task.setDescription(request.description());
    task.setStatus(request.status());
    task.setStartDate(request.startDate());
    task.setDueDate(request.dueDate());
    task.setPriority(request.priority());
    task.setProgress(request.progress());
    task.setCreatedAt(now);
    task.setUpdatedAt(now);
  }

  public static void applyUpdate(Task task, TaskUpdateRequest request) {
    if (request.taskType() != null) {
      task.setTaskType(request.taskType());
    }
    if (request.title() != null) {
      task.setTitle(request.title());
    }
    if (request.description() != null) {
      task.setDescription(request.description());
    }
    if (request.status() != null) {
      task.setStatus(request.status());
    }
    if (request.startDate() != null) {
      task.setStartDate(request.startDate());
    }
    if (request.dueDate() != null) {
      task.setDueDate(request.dueDate());
    }
    if (request.priority() != null) {
      task.setPriority(request.priority());
    }
    if (request.progress() != null) {
      task.setProgress(request.progress());
    }
    task.setUpdatedAt(LocalDateTime.now());
  }
}
