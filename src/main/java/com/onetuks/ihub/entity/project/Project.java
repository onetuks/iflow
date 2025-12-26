package com.onetuks.ihub.entity.project;

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
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = Project.TABLE_NAME)
@Getter
@Setter
public class Project {

  public static final String TABLE_NAME = "projects";

  @Id
  @Column(name = "project_id", nullable = false)
  private String projectId;

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "description")
  private String description;

  @Column(name = "start_date")
  private LocalDate startDate;

  @Column(name = "end_date")
  private LocalDate endDate;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "created_by", referencedColumnName = "email", nullable = false)
  private User createdBy;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "current_admin_id", referencedColumnName = "email", nullable = false)
  private User currentAdmin;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "status", nullable = false)
  private ProjectStatus status;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;
}
