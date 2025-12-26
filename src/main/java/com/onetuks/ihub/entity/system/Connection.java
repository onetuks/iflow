package com.onetuks.ihub.entity.system;

import com.onetuks.ihub.entity.project.Project;
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
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = Connection.TABLE_NAME)
@Getter
@Setter
public class Connection {

  public static final String TABLE_NAME = "connections";

  @Id
  @Column(name = "connection_id", nullable = false)
  private String connectionId;

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

  @JdbcTypeCode(value = SqlTypes.JSON)
  @Column(name = "extra_config", columnDefinition = "json")
  private String extraConfig;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "status", nullable = false)
  private ConnectionStatus status;

  @Column(name = "description")
  private String description;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "created_by", referencedColumnName = "email", nullable = false)
  private User createdBy;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "updated_by", referencedColumnName = "email", nullable = false)
  private User updatedBy;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;
}
