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
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = ProjectMember.TABLE_NAME, uniqueConstraints = {
    @UniqueConstraint(name = "unq_project_user", columnNames = {"project_id", "email"})})
@Getter
@Setter
public class ProjectMember {

  public static final String TABLE_NAME = "project_members";

  @Id
  @Column(name = "project_member_id", nullable = false)
  private String projectMemberId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "project_id", referencedColumnName = "project_id", nullable = false)
  private Project project;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "email", referencedColumnName = "email", nullable = false)
  private User user;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "role", nullable = false)
  private ProjectMemberRole role;

  @Column(name = "joined_at")
  private LocalDateTime joinedAt;

  @Column(name = "left_at")
  private LocalDateTime leftAt;
}
