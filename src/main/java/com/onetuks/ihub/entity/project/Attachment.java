package com.onetuks.ihub.entity.project;

import com.onetuks.ihub.entity.communication.TargetType;
import com.onetuks.ihub.entity.file.File;
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
@Table(name = Attachment.TABLE_NAME)
@Getter
@Setter
public class Attachment {

  public static final String TABLE_NAME = "attachments";

  @Id
  @Column(name = "attachment_id", nullable = false)
  private String attachmentId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "project_id", referencedColumnName = "project_id", nullable = false)
  private Project project;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "file_id", referencedColumnName = "file_id", nullable = false)
  private File file;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "target_type", nullable = false)
  private TargetType targetType;

  @Column(name = "target_id", nullable = false)
  private String targetId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "attached_by", referencedColumnName = "email", nullable = false)
  private User attachedBy;

  @Column(name = "attached_at")
  private LocalDateTime attachedAt;
}
