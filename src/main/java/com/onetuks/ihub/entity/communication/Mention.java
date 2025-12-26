package com.onetuks.ihub.entity.communication;

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
@Table(name = Mention.TABLE_NAME)
@Getter
@Setter
public class Mention {

  public static final String TABLE_NAME = "mentions";

  @Id
  @Column(name = "mention_id", nullable = false)
  private String mentionId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "project_id", referencedColumnName = "project_id", nullable = false)
  private Project project;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "target_type")
  private TargetType targetType;

  @Column(name = "target_id")
  private String targetId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
      name = "mentioned_email",
      referencedColumnName = "email",
      nullable = false)
  private User mentionedUser;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "created_by", referencedColumnName = "email", nullable = false)
  private User createdBy;

  @Column(name = "created_at")
  private LocalDateTime createdAt;
}
