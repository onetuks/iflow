package com.onetuks.ihub.entity.task;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = TaskFilterGroupStatus.TABLE_NAME, uniqueConstraints = {
    @UniqueConstraint(name = "unq_group_status", columnNames = {"group_id", "status_type"})})
@Getter
@Setter
public class TaskFilterGroupStatus {

  public static final String TABLE_NAME = "task_filter_group_statuses";

  @Id
  @Column(name = "status_id", nullable = false)
  private String statusId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "group_id", referencedColumnName = "group_id", nullable = false)
  private TaskFilterGroup group;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "status_type", nullable = false)
  private TaskFilterGroupStatusType statusType;

  @Column(name = "created_at")
  private LocalDateTime createdAt;
}
