package com.onetuks.ihub.controller.project;

import com.onetuks.ihub.dto.project.ProjectCreateRequest;
import com.onetuks.ihub.dto.project.ProjectResponse;
import com.onetuks.ihub.dto.project.ProjectUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/projects")
@Tag(name = "Project", description = "Project management APIs")
public interface ProjectRestController {

  @Operation(summary = "Create project")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Project created"),
    @ApiResponse(responseCode = "400", description = "Invalid request"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PostMapping
  ResponseEntity<ProjectResponse> createProject(
      @Valid @RequestBody ProjectCreateRequest request);

  @Operation(summary = "Get project by id")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Project found"),
    @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
    @ApiResponse(responseCode = "404", description = "Project not found"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @GetMapping("/{projectId}")
  ResponseEntity<ProjectResponse> getProject(@PathVariable Long projectId);

  @Operation(summary = "List projects")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Projects listed"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @GetMapping
  ResponseEntity<List<ProjectResponse>> getProjects();

  @Operation(summary = "Update project")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Project updated"),
    @ApiResponse(responseCode = "400", description = "Invalid request"),
    @ApiResponse(responseCode = "404", description = "Project not found"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PutMapping("/{projectId}")
  ResponseEntity<ProjectResponse> updateProject(
      @PathVariable Long projectId,
      @Valid @RequestBody ProjectUpdateRequest request);

  @Operation(summary = "Delete project")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Project deleted"),
    @ApiResponse(responseCode = "400", description = "Invalid id supplied"),
    @ApiResponse(responseCode = "404", description = "Project not found"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @DeleteMapping("/{projectId}")
  ResponseEntity<Void> deleteProject(@PathVariable Long projectId);
}
