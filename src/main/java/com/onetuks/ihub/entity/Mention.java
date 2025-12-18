package com.onetuks.ihub.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "mentions")
@RequiredArgsConstructor
public class Mention {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "mention_id", nullable = false)
  private Long mentionId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "project_id", referencedColumnName = "project_id", nullable = false)
  private Project project;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "target_type")
  private TargetType targetType;

  @Column(name = "target_id")
  private Long targetId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
      name = "mentioned_user_id",
      referencedColumnName = "user_id",
      nullable = false)
  private User mentionedUser;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "created_by", referencedColumnName = "user_id", nullable = false)
  private User createdBy;

  @Column(name = "created_at")
  private LocalDateTime createdAt;
}
