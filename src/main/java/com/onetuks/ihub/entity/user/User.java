package com.onetuks.ihub.entity.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = User.TABLE_NAME)
@Getter
@Setter
public class User {

  public static final String TABLE_NAME = "users";

  @Id
  @Column(name = "email", unique = true, nullable = false)
  private String email;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "company")
  private String company; // 회사명

  @Column(name = "position")
  private String position; // 직함

  @Column(name = "phone_number")
  private String phoneNumber; // 휴대전화번호

  @Column(name = "profile_image_url")
  private String profileImageUrl; // 프로필 사진

  @Enumerated(value = EnumType.STRING)
  @Column(name = "status", nullable = false)
  private UserStatus status;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;
}
