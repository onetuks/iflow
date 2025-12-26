package com.onetuks.ihub.service.file;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.onetuks.ihub.TestcontainersConfiguration;
import com.onetuks.ihub.dto.file.FolderCreateRequest;
import com.onetuks.ihub.dto.file.FolderResponse;
import com.onetuks.ihub.dto.file.FolderUpdateRequest;
import com.onetuks.ihub.entity.project.Project;
import com.onetuks.ihub.entity.user.User;
import com.onetuks.ihub.mapper.FolderMapper;
import com.onetuks.ihub.repository.FolderJpaRepository;
import com.onetuks.ihub.repository.ProjectJpaRepository;
import com.onetuks.ihub.repository.UserJpaRepository;
import com.onetuks.ihub.service.ServiceTestDataFactory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class FolderServiceTest {

  @Autowired
  private FolderService folderService;

  @Autowired
  private FolderJpaRepository folderJpaRepository;

  @Autowired
  private ProjectJpaRepository projectJpaRepository;

  @Autowired
  private UserJpaRepository userJpaRepository;

  private Project project;
  private User creator;

  @BeforeEach
  void setUp() {
    creator = ServiceTestDataFactory.createUser(userJpaRepository);
    project = ServiceTestDataFactory.createProject(projectJpaRepository, creator, creator, "FolderProj");
  }

  @AfterEach
  void tearDown() {
    folderJpaRepository.deleteAll();
    projectJpaRepository.deleteAll();
    userJpaRepository.deleteAll();
  }

  @Test
  void createFolder_success() {
    FolderCreateRequest request = new FolderCreateRequest(
        project.getProjectId(),
        null,
        "Root",
        creator.getEmail());

    FolderResponse response = FolderMapper.toResponse(folderService.create(request));

    assertNotNull(response.folderId());
    assertEquals("Root", response.name());
    assertEquals(project.getProjectId(), response.projectId());
  }

  @Test
  void updateFolder_success() {
    FolderResponse created = FolderMapper.toResponse(folderService.create(new FolderCreateRequest(
        project.getProjectId(), null, "Parent", creator.getEmail())));
    FolderUpdateRequest updateRequest = new FolderUpdateRequest(null, "ParentRenamed");

    FolderResponse updated = FolderMapper.toResponse(folderService.update(created.folderId(), updateRequest));

    assertEquals("ParentRenamed", updated.name());
  }

  @Test
  void getFolders_returnsAll() {
    folderService.create(new FolderCreateRequest(
        project.getProjectId(), null, "F1", creator.getEmail()));
    folderService.create(new FolderCreateRequest(
        project.getProjectId(), null, "F2", creator.getEmail()));

    assertEquals(2, folderService.getAll().size());
  }

  @Test
  void deleteFolder_success() {
    FolderResponse created = FolderMapper.toResponse(folderService.create(new FolderCreateRequest(
        project.getProjectId(), null, "Del", creator.getEmail())));

    folderService.delete(created.folderId());

    assertEquals(0, folderJpaRepository.count());
    assertThrows(EntityNotFoundException.class, () -> folderService.getById(created.folderId()));
  }
}
