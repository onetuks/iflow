package com.onetuks.ihub.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "interface_status_histories")
@RequiredArgsConstructor
public class InterfaceStatusHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "history_id", nullable = false)
  private Long historyId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "interface_id", referencedColumnName = "interface_id", nullable = false)
  private Interface anInterface;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "from_status_id", referencedColumnName = "status_id", nullable = false)
  private InterfaceStatus fromStatus;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "to_status_id", referencedColumnName = "status_id", nullable = false)
  private InterfaceStatus toStatus;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "changed_by", referencedColumnName = "user_id", nullable = false)
  private User changedBy;

  @Column(name = "changed_at")
  private LocalDateTime changedAt;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "related_task_id", referencedColumnName = "task_id", nullable = false)
  private Task relatedTask;

  @Column(name = "reason")
  private String reason;
}
