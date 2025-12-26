package com.onetuks.ihub.repository;

import com.onetuks.ihub.entity.system.Connection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectionJpaRepository extends JpaRepository<Connection, String> {
}
