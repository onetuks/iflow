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
@Table(name = FeedItem.TABLE_NAME)
@Getter
@Setter
public class FeedItem {

  public static final String TABLE_NAME = "feed_items";

  @Id
  @Column(name = "feed_id", nullable = false)
  private String feedId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "project_id", referencedColumnName = "project_id", nullable = false)
  private Project project;

  @Column(name = "event_type")
  private String eventType;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "actor_id", referencedColumnName = "email")
  private User actor;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "target_type")
  private TargetType targetType;

  @Column(name = "target_id")
  private String targetId;

  @Column(name = "summary")
  private String summary;

  @Column(name = "created_at")
  private LocalDateTime createdAt;
}
