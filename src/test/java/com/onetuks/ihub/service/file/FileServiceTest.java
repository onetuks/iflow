package com.onetuks.ihub.service.file;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.onetuks.ihub.TestcontainersConfiguration;
import com.onetuks.ihub.dto.file.FileCreateRequest;
import com.onetuks.ihub.dto.file.FileResponse;
import com.onetuks.ihub.dto.file.FileUpdateRequest;
import com.onetuks.ihub.dto.file.FolderCreateRequest;
import com.onetuks.ihub.dto.file.FolderResponse;
import com.onetuks.ihub.entity.file.FileStatus;
import com.onetuks.ihub.entity.project.Project;
import com.onetuks.ihub.entity.user.User;
import com.onetuks.ihub.mapper.FileMapper;
import com.onetuks.ihub.mapper.FolderMapper;
import com.onetuks.ihub.repository.FileJpaRepository;
import com.onetuks.ihub.repository.FolderJpaRepository;
import com.onetuks.ihub.repository.ProjectJpaRepository;
import com.onetuks.ihub.repository.UserJpaRepository;
import com.onetuks.ihub.service.ServiceTestDataFactory;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class FileServiceTest {

  @Autowired
  private FileService fileService;

  @Autowired
  private FolderService folderService;

  @Autowired
  private FileJpaRepository fileJpaRepository;

  @Autowired
  private FolderJpaRepository folderJpaRepository;

  @Autowired
  private ProjectJpaRepository projectJpaRepository;

  @Autowired
  private UserJpaRepository userJpaRepository;

  private Project project;
  private User uploader;
  private String folderId;

  @BeforeEach
  void setUp() {
    uploader = ServiceTestDataFactory.createUser(userJpaRepository, "file@user.com", "FileUser");
    project = ServiceTestDataFactory.createProject(projectJpaRepository, uploader, uploader, "FileProj");
    FolderResponse folderResponse = FolderMapper.toResponse(folderService.create(new FolderCreateRequest(
        project.getProjectId(), null, "Docs", uploader.getEmail())));
    folderId = folderResponse.folderId();
  }

  @AfterEach
  void tearDown() {
    fileJpaRepository.deleteAll();
    folderJpaRepository.deleteAll();
    projectJpaRepository.deleteAll();
    userJpaRepository.deleteAll();
  }

  @Test
  void createFile_success() {
    FileCreateRequest request = new FileCreateRequest(
        project.getProjectId(),
        folderId,
        FileStatus.ACTIVE,
        "orig.txt",
        "stored.txt",
        123L,
        "text/plain",
        uploader.getEmail());

    FileResponse response = FileMapper.toResponse(fileService.create(request));

    assertNotNull(response.fileId());
    assertEquals("orig.txt", response.originalName());
    assertEquals(FileStatus.ACTIVE, response.status());
  }

  @Test
  void updateFile_success() {
    FileResponse created = FileMapper.toResponse(fileService.create(new FileCreateRequest(
        project.getProjectId(),
        folderId,
        FileStatus.ACTIVE,
        "orig2.txt",
        "stored2.txt",
        200L,
        "text/plain",
        uploader.getEmail())));

    FileUpdateRequest updateRequest = new FileUpdateRequest(
        null,
        FileStatus.DELETED,
        "orig2_new.txt",
        "stored2_new.txt",
        250L,
        "text/markdown",
        uploader.getEmail(),
        LocalDateTime.now());

    FileResponse updated = FileMapper.toResponse(fileService.update(created.fileId(), updateRequest));

    assertEquals(FileStatus.DELETED, updated.status());
    assertEquals("orig2_new.txt", updated.originalName());
    assertNotNull(updated.deletedAt());
  }

  @Test
  void getFiles_returnsAll() {
    fileService.create(new FileCreateRequest(
        project.getProjectId(), null, FileStatus.ACTIVE, "a", "a", 1L, "text", uploader.getEmail()));
    fileService.create(new FileCreateRequest(
        project.getProjectId(), null, FileStatus.ACTIVE, "b", "b", 1L, "text", uploader.getEmail()));

    assertEquals(2, fileService.getAll().size());
  }

  @Test
  void deleteFile_success() {
    FileResponse created = FileMapper.toResponse(fileService.create(new FileCreateRequest(
        project.getProjectId(), null, FileStatus.ACTIVE, "c", "c", 1L, "text", uploader.getEmail())));

    fileService.delete(created.fileId());

    assertEquals(0, fileJpaRepository.count());
    assertThrows(EntityNotFoundException.class, () -> fileService.getById(created.fileId()));
  }
}
