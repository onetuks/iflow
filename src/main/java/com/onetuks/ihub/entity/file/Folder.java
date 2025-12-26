package com.onetuks.ihub.entity.file;

import com.onetuks.ihub.entity.project.Project;
import com.onetuks.ihub.entity.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = Folder.TABLE_NAME)
@Getter
@Setter
public class Folder {

  public static final String TABLE_NAME = "folders";

  @Id
  @Column(name = "folder_id", nullable = false)
  private String folderId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "project_id", referencedColumnName = "project_id", nullable = false)
  private Project project;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_folder_id", referencedColumnName = "folder_id")
  private Folder parentFolder;

  @Column(name = "name")
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "created_by", referencedColumnName = "email")
  private User createdBy;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;
}
