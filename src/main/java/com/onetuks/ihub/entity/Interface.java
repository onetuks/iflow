package com.onetuks.ihub.entity;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "interfaces")
@RequiredArgsConstructor
public class Interface {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "interface_id", nullable = false)
  private Long interfaceId;

  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "project_id", referencedColumnName = "project_id", nullable = false)
  private Project project;

  @Column(name = "if_id")
  private String ifId;

  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "source_system_id", referencedColumnName = "system_id", nullable = false)
  private System sourceSystem;

  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "target_system_id", referencedColumnName = "system_id", nullable = false)
  private System targetSystem;

  @Column(name = "module")
  private String module;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "interface_type", nullable = false)
  private InterfaceType interfaceType;

  @Column(name = "pattern")
  private String pattern;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "sender_adapter", nullable = false)
  private ChannelAdapter senderAdapter;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "receiver_adapter", nullable = false)
  private ChannelAdapter receiverAdapter;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "sa", nullable = false)
  private SyncAsyncType sa;

  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "status_id", referencedColumnName = "status_id", nullable = false)
  private InterfaceStatus status;

  @Column(name = "batch_time_label")
  private String batchTimeLabel;

  @Column(name = "remark")
  private String remark;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "created_by", referencedColumnName = "user_id", nullable = false)
  private User createdBy;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;
}
