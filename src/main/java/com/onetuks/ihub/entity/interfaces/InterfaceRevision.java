package com.onetuks.ihub.entity.interfaces;

import com.onetuks.ihub.entity.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = InterfaceRevision.TABLE_NAME)
@Getter
@Setter
public class InterfaceRevision {

  public static final String TABLE_NAME = "interface_revisions";

  @Id
  @Column(name = "revision_id", nullable = false)
  private String revisionId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "interface_id", referencedColumnName = "interface_id", nullable = false)
  private Interface anInterface;

  @Column(name = "version_no", nullable = false)
  private Integer versionNo;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "changed_by", referencedColumnName = "email", nullable = false)
  private User changedBy;

  @Column(name = "changed_at")
  private LocalDateTime changedAt;

  @JdbcTypeCode(value = SqlTypes.JSON)
  @Column(name = "snapshot", columnDefinition = "json")
  private Map<String, String> snapshot;

  @Column(name = "reason")
  private String reason;
}
