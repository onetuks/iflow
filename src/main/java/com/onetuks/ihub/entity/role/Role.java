package com.onetuks.ihub.entity.role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/*
USER_FULL_ACCESS: 계정 다건조회/단건조회/생성/수정
PROJECT_PERSONAL_ACCESS: 프로젝트 단건조회/생성/수정/권한수정 (내 플젝만 가능), 멤버삭제
SYSTEM_PERSONAL_ACCESS: 시스템 다건조회/생성/수정/삭제(내 플젝만 가능)
TASK_FULL_ACCESS: 일감 다건조회/단건조회
POST_FULL_ACCESS: 다건조회/단건조회/생성/수정/삭제
 */
@Entity
@Table(name = Role.TABLE_NAME)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {

  public static final String TABLE_NAME = "roles";

  @Id
  @Column(name = "role_id", nullable = false)
  private String roleId;

  @Column(name = "role_name", nullable = false)
  private String roleName;

  @Column(name = "description")
  private String description;

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Role role = (Role) o;
    return roleId.equals(role.roleId);
  }

  @Override
  public int hashCode() {
    return roleId.hashCode();
  }
}
