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
@Table(name = "systems")
@RequiredArgsConstructor
public class System {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "system_id", nullable = false)
  private Long systemId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "project_id", referencedColumnName = "project_id", nullable = false)
  private Project project;

  @Column(name = "system_code")
  private String systemCode;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "status", nullable = false)
  private SystemStatus status;

  @Column(name = "description")
  private String description;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "system_type", nullable = false)
  private SystemType systemType;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "environment", nullable = false)
  private SystemEnvironment environment;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "created_by", referencedColumnName = "user_id", nullable = false)
  private User createdBy;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "updated_by", referencedColumnName = "user_id", nullable = false)
  private User updatedBy;

  @Column(name = "created_by")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;
}
