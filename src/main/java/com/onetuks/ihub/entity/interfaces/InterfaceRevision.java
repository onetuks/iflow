package com.onetuks.ihub.entity.interfaces;

import com.onetuks.ihub.entity.user.User;
import io.hypersistence.utils.hibernate.type.json.JsonType;
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
import org.hibernate.annotations.Type;

@Entity
@Table(name = "interface_revisions")
@RequiredArgsConstructor
public class InterfaceRevision {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "revision_id", nullable = false)
  private Long revisionId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "interface_id", referencedColumnName = "interface_id", nullable = false)
  private Interface anInterface;

  @Column(name = "version_no", nullable = false)
  private Integer versionNo;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "changed_by", referencedColumnName = "user_id", nullable = false)
  private User changedBy;

  @Column(name = "changed_at")
  private LocalDateTime changedAt;

  @Type(value = JsonType.class)
  @Column(name = "snapshot", columnDefinition = "json")
  private String snapshot;

  @Column(name = "reason")
  private String reason;
}
