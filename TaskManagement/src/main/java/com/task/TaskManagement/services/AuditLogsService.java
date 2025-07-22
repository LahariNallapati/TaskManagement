package com.task.TaskManagement.services;

import com.task.TaskManagement.Entity.AuditLogsEntity;
import com.task.TaskManagement.Entity.ClientEntity;
import com.task.TaskManagement.Entity.ProjectsEntity;
import com.task.TaskManagement.dto.AuditLogsdto;
import com.task.TaskManagement.dto.Clientdto;
import com.task.TaskManagement.dto.ResponseWrapper;

import java.time.LocalDateTime;
import java.util.List;

public interface AuditLogsService {

    ResponseWrapper<AuditLogsEntity> createAuditLog(AuditLogsdto dto);
    ResponseWrapper<AuditLogsEntity> updateAuditLog(Integer id, AuditLogsdto dto);
    //ResponseWrapper<String> deleteAuditLog(Integer id);
    ResponseWrapper<String> deleteAuditLog(Integer id);
    ResponseWrapper<AuditLogsEntity> getAuditLogById(Integer id);
    ResponseWrapper<List<AuditLogsEntity>> getAllAuditLogs();
    List<AuditLogsEntity> getAuditsPageOnly(int pageNo, int pageSize);
    List<AuditLogsEntity> filterAuditLogs(String userName, String operation, LocalDateTime startDate, LocalDateTime endDate);


}
