package com.onetuks.ihub.service.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.onetuks.ihub.TestcontainersConfiguration;
import com.onetuks.ihub.dto.file.FileCreateRequest;
import com.onetuks.ihub.dto.file.FileResponse;
import com.onetuks.ihub.dto.project.AttachmentCreateRequest;
import com.onetuks.ihub.dto.project.AttachmentResponse;
import com.onetuks.ihub.dto.project.AttachmentUpdateRequest;
import com.onetuks.ihub.entity.communication.TargetType;
import com.onetuks.ihub.entity.file.FileStatus;
import com.onetuks.ihub.entity.project.Project;
import com.onetuks.ihub.entity.user.User;
import com.onetuks.ihub.mapper.AttachmentMapper;
import com.onetuks.ihub.mapper.FileMapper;
import com.onetuks.ihub.repository.AttachmentJpaRepository;
import com.onetuks.ihub.repository.FileJpaRepository;
import com.onetuks.ihub.repository.ProjectJpaRepository;
import com.onetuks.ihub.repository.UserJpaRepository;
import com.onetuks.ihub.service.ServiceTestDataFactory;
import com.onetuks.ihub.service.file.FileService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class AttachmentServiceTest {

  @Autowired
  private AttachmentService attachmentService;

  @Autowired
  private FileService fileService;

  @Autowired
  private AttachmentJpaRepository attachmentJpaRepository;

  @Autowired
  private FileJpaRepository fileJpaRepository;

  @Autowired
  private ProjectJpaRepository projectJpaRepository;

  @Autowired
  private UserJpaRepository userJpaRepository;

  private Project project;
  private User user;
  private String fileId;

  @BeforeEach
  void setUp() {
    user = ServiceTestDataFactory.createUser(userJpaRepository, "attach@user.com", "AttachUser");
    project = ServiceTestDataFactory.createProject(projectJpaRepository, user, user, "AttachProj");
    FileResponse file = FileMapper.toResponse(fileService.create(new FileCreateRequest(
        project.getProjectId(), null, FileStatus.ACTIVE, "orig", "stored", 10L, "text", user.getEmail())));
    fileId = file.fileId();
  }

  @AfterEach
  void tearDown() {
    attachmentJpaRepository.deleteAll();
    fileJpaRepository.deleteAll();
    projectJpaRepository.deleteAll();
    userJpaRepository.deleteAll();
  }

  @Test
  void createAttachment_success() {
    AttachmentCreateRequest request = new AttachmentCreateRequest(
        project.getProjectId(),
        fileId,
        TargetType.POST,
        "1L",
        user.getEmail());

    AttachmentResponse response = AttachmentMapper.toResponse(attachmentService.create(request));

    assertNotNull(response.attachmentId());
    assertEquals(fileId, response.fileId());
    assertEquals(TargetType.POST, response.targetType());
  }

  @Test
  void updateAttachment_success() {
    AttachmentResponse created = AttachmentMapper.toResponse(attachmentService.create(
        new AttachmentCreateRequest(
            project.getProjectId(), fileId, TargetType.POST, "1L", user.getEmail())));
    FileResponse newFile = FileMapper.toResponse(fileService.create(new FileCreateRequest(
        project.getProjectId(), null, FileStatus.ACTIVE, "orig2", "stored2", 20L, "text", user.getEmail())));

    AttachmentUpdateRequest updateRequest = new AttachmentUpdateRequest(
        newFile.fileId(),
        TargetType.TASK,
        "2L",
        user.getEmail());

    AttachmentResponse updated =
        AttachmentMapper.toResponse(attachmentService.update(created.attachmentId(), updateRequest));

    assertEquals(TargetType.TASK, updated.targetType());
    assertEquals(2L, updated.targetId());
    assertEquals(newFile.fileId(), updated.fileId());
  }

  @Test
  void getAttachments_returnsAll() {
    attachmentService.create(new AttachmentCreateRequest(
        project.getProjectId(), fileId, TargetType.POST, "1L", user.getEmail()));
    attachmentService.create(new AttachmentCreateRequest(
        project.getProjectId(), fileId, TargetType.TASK, "2L", user.getEmail()));

    assertEquals(2, attachmentService.getAll().size());
  }

  @Test
  void deleteAttachment_success() {
    AttachmentResponse created = AttachmentMapper.toResponse(attachmentService.create(
        new AttachmentCreateRequest(
            project.getProjectId(), fileId, TargetType.POST, "1L", user.getEmail())));

    attachmentService.delete(created.attachmentId());

    assertEquals(0, attachmentJpaRepository.count());
    assertThrows(EntityNotFoundException.class,
        () -> attachmentService.getById(created.attachmentId()));
  }
}
