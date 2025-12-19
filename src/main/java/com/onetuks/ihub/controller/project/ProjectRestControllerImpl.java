package com.onetuks.ihub.controller.project;

import com.onetuks.ihub.dto.project.ProjectCreateRequest;
import com.onetuks.ihub.dto.project.ProjectResponse;
import com.onetuks.ihub.dto.project.ProjectUpdateRequest;
import com.onetuks.ihub.service.project.ProjectService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProjectRestControllerImpl implements ProjectRestController {

  private final ProjectService projectService;

  @Override
  public ResponseEntity<ProjectResponse> createProject(
      @Valid @RequestBody ProjectCreateRequest request) {
    ProjectResponse response = projectService.create(request);
    return ResponseEntity.created(URI.create("/api/projects/" + response.projectId()))
        .body(response);
  }

  @Override
  public ResponseEntity<ProjectResponse> getProject(@PathVariable Long projectId) {
    return ResponseEntity.ok(projectService.getById(projectId));
  }

  @Override
  public ResponseEntity<List<ProjectResponse>> getProjects() {
    return ResponseEntity.ok(projectService.getAll());
  }

  @Override
  public ResponseEntity<ProjectResponse> updateProject(
      @PathVariable Long projectId,
      @Valid @RequestBody ProjectUpdateRequest request) {
    return ResponseEntity.ok(projectService.update(projectId, request));
  }

  @Override
  public ResponseEntity<Void> deleteProject(@PathVariable Long projectId) {
    projectService.delete(projectId);
    return ResponseEntity.noContent().build();
  }
}
