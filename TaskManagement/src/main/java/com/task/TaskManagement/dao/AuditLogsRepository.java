package com.task.TaskManagement.dao;

import com.task.TaskManagement.Entity.AuditLogsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogsRepository extends JpaRepository<AuditLogsEntity, Integer> {
}
