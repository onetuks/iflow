package com.onetuks.ihub.entity.communication;

import com.onetuks.ihub.entity.project.Project;
import com.onetuks.ihub.entity.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = Post.TABLE_NAME)
@Getter
@Setter
public class Post {

  public static final String TABLE_NAME = "posts";

  @Id
  @Column(name = "post_id", nullable = false)
  private String postId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "project_id", referencedColumnName = "project_id", nullable = false)
  private Project project;

  @Column(name = "title")
  private String title;

  @Column(name = "content")
  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "created_by", referencedColumnName = "email")
  private User createdBy;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;
}
