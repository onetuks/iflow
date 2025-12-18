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
@Table(name = "feed_items")
@RequiredArgsConstructor
public class FeedItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "feed_id", nullable = false)
  private Long feedId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "project_id", referencedColumnName = "project_id", nullable = false)
  private Project project;

  @Column(name = "event_type")
  private String eventType;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "actor_id", referencedColumnName = "user_id")
  private User actor;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "target_type")
  private TargetType targetType;

  @Column(name = "target_id")
  private Long targetId;

  @Column(name = "summary")
  private String summary;

  @Column(name = "created_at")
  private LocalDateTime createdAt;
}
