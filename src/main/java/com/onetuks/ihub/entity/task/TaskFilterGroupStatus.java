package com.onetuks.ihub.entity.task;

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
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "task_filter_group_statuses", uniqueConstraints = {
    @UniqueConstraint(name = "unq_group_status", columnNames = {"group_id", "status_type"})})
@RequiredArgsConstructor
public class TaskFilterGroupStatus {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "status_id", nullable = false)
  private Long statusId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "group_id", referencedColumnName = "group_id", nullable = false)
  private TaskFilterGroup group;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "status_type", nullable = false)
  private TaskFilterGroupStatusType statusType;

  @Column(name = "created_at")
  private LocalDateTime createdAt;
}
