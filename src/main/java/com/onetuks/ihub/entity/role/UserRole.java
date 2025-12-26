package com.onetuks.ihub.entity.role;

import com.onetuks.ihub.entity.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = UserRole.TABLE_NAME, uniqueConstraints = {
    @UniqueConstraint(name = "unq_user_role", columnNames = {"email", "role_id"})})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRole {

  public static final String TABLE_NAME = "user_roles";

  @Id
  @Column(name = "user_role_id", nullable = false)
  private String userRoleId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "email", referencedColumnName = "email", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "role_id", referencedColumnName = "role_id", nullable = false)
  private Role role;
}
