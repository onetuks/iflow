package com.onetuks.ihub.entity.interfaces;

import com.onetuks.ihub.entity.project.Project;
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
@Table(name = InterfaceStatus.TABLE_NAME)
@Getter
@Setter
public class InterfaceStatus {

  public static final String TABLE_NAME = "interface_statuses";

  @Id
  @Column(name = "status_id", nullable = false)
  private String statusId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "project_id", referencedColumnName = "project_id", nullable = false)
  private Project project;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "code")
  private String code;

  @Column(name = "seq_order", nullable = false)
  private Integer seqOrder;

  @Column(name = "is_default", nullable = false)
  private Boolean isDefault;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;
}
