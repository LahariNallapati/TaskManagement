package com.task.TaskManagement.dao;

import com.task.TaskManagement.Entity.AuditLogsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AuditLogsRepository extends JpaRepository<AuditLogsEntity, Integer> {
    @Query("""
        SELECT a FROM AuditLogsEntity a
        LEFT JOIN FETCH a.user u
        WHERE (:userName IS NULL OR LOWER(u.userName) = LOWER(:userName))
        AND (:operation IS NULL OR LOWER(a.operation) = LOWER(:operation))
        AND (:startDate IS NULL OR a.createdAt >= :startDate)
        AND (:endDate IS NULL OR a.createdAt <= :endDate)
        """)
    List<AuditLogsEntity> filterAuditLogs(
            @Param("userName") String userName,
            @Param("operation") String operation,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

}
