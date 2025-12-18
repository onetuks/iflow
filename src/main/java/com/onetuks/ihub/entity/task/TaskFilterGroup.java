package com.onetuks.ihub.entity.task;

import com.onetuks.ihub.entity.user.User;
import com.onetuks.ihub.entity.project.Project;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "task_filter_groups")
@RequiredArgsConstructor
public class TaskFilterGroup {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "group_id", nullable = false)
  private Long groupId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
  private User user;

  @Column(name = "name", nullable = false)
  private String name;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "project_id", referencedColumnName = "project_id", nullable = false)
  private Project project;

  @Column(name = "assignee_keyword")
  private String assigneeKeyword;

  @Column(name = "author_keyword")
  private String authorKeyword;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "date_type", nullable = false)
  private TaskFilterGroupDateType dateType;

  @Column(name = "date_from")
  private LocalDate dateFrom;

  @Column(name = "date_to")
  private LocalDate dateTo;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;
}
