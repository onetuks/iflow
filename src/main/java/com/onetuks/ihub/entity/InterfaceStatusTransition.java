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
@Table(name = "interface_status_transitions")
@RequiredArgsConstructor
public class InterfaceStatusTransition {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "transition_id", nullable = false)
  private Long transitionId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "project_id", referencedColumnName = "project_id", nullable = false)
  private Project project;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "from_status_id", referencedColumnName = "status_id", nullable = false)
  private InterfaceStatus fromStatus;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "to_status_id", referencedColumnName = "status_id", nullable = false)
  private InterfaceStatus toStatus;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "allowed_role", nullable = false)
  private InterfaceRole allowedRole;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "status", nullable = false)
  private InterfaceStatusTransitionStatus status;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "created_by", referencedColumnName = "user_id", nullable = false)
  private User createdBy;
}
