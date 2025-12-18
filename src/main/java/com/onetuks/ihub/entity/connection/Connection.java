package com.onetuks.ihub.entity.connection;

import com.onetuks.ihub.entity.project.Project;
import com.onetuks.ihub.entity.system.System;
import com.onetuks.ihub.entity.user.User;
import io.hypersistence.utils.hibernate.type.json.JsonType;
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
import org.hibernate.annotations.Type;

@Entity
@Table(name = "connections")
@RequiredArgsConstructor
public class Connection {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "connection_id", nullable = false)
  private Long connectionId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "project_id", referencedColumnName = "project_id", nullable = false)
  private Project project;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "system_id", referencedColumnName = "system_id", nullable = false)
  private System system;

  @Column(name = "name")
  private String name;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "protocol")
  private Protocol protocol;

  @Column(name = "host")
  private String host;

  @Column(name = "port")
  private Integer port;

  @Column(name = "path")
  private String path;

  @Column(name = "username")
  private String username;

  @Column(name = "auth_type")
  private String authType;

  @Type(value = JsonType.class)
  @Column(name = "extra_config", columnDefinition = "json")
  private String extraConfig;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "status", nullable = false)
  private ConnectionStatus status;

  @Column(name = "description")
  private String description;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "created_by", referencedColumnName = "user_id", nullable = false)
  private User createdBy;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "updated_by", referencedColumnName = "user_id", nullable = false)
  private User updatedBy;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;
}
