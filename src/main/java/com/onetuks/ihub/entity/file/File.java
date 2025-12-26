package com.onetuks.ihub.entity.file;

import com.onetuks.ihub.entity.project.Project;
import com.onetuks.ihub.entity.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = File.TABLE_NAME)
@Getter
@Setter
public class File {

  public static final String TABLE_NAME = "files";

  @Id
  @Column(name = "file_id", nullable = false)
  private String fileId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "project_id", referencedColumnName = "project_id", nullable = false)
  private Project project;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "folder_id", referencedColumnName = "folder_id")
  private Folder folder;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "status", nullable = false)
  private FileStatus status;

  @Column(name = "original_name")
  private String originalName;

  @Column(name = "stored_name")
  private String storedName;

  @Column(name = "size_bytes")
  private Long sizeBytes;

  @Column(name = "mime_type")
  private String mimeType;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "uploaded_by", referencedColumnName = "email")
  private User uploadedBy;

  @Column(name = "uploaded_at")
  private LocalDateTime uploadedAt;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;
}
