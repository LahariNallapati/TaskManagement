package com.task.TaskManagement.services.Impl;

import com.task.TaskManagement.Entity.*;
import com.task.TaskManagement.dao.AuditLogsRepository;
import com.task.TaskManagement.dao.UsersRepository;
import com.task.TaskManagement.dto.AuditLogsdto;
import com.task.TaskManagement.dto.ResponseWrapper;
import com.task.TaskManagement.services.AuditLogsService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditLogServiceImpl implements AuditLogsService {

@Autowired
    private AuditLogsRepository auditLogsRepository;

@Autowired
    private UsersRepository usersRepository;

    @Override
    public ResponseWrapper<AuditLogsEntity> createAuditLog(AuditLogsdto dto) {
        UsersEntity user = usersRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Users not found with id: " + dto.getUserId()));

        AuditLogsEntity auditLogs = new AuditLogsEntity();
        BeanUtils.copyProperties(dto,auditLogs, "logId"); // Automatically maps matching fields
        auditLogs.setUser(user);
        auditLogs.setCreatedAt(LocalDateTime.now());
        //project.setUpdatedAt(LocalDateTime.now());

        AuditLogsEntity saved = auditLogsRepository.save(auditLogs);
        return new ResponseWrapper<>("AuditLogs created successfully", saved);
    }

    @Override
    public ResponseWrapper<AuditLogsEntity> getAuditLogById(Integer id) {
       AuditLogsEntity auditLogs = auditLogsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Audits not found"));
        return new ResponseWrapper<>("AuditLogs fetched successfully", auditLogs);
    }

    @Override
    public ResponseWrapper<List<AuditLogsEntity>> getAllAuditLogs() {
        List<AuditLogsEntity> list = auditLogsRepository.findAll();
        return new ResponseWrapper<>("All Audits fetched successfully", list);
    }

    @Override
    public List<AuditLogsEntity> getAuditsPageOnly(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<AuditLogsEntity> task =auditLogsRepository.findAll(pageable);
        return task.getContent();
    }

    @Override
    public List<AuditLogsEntity> filterAuditLogs(String userName, String operation, LocalDateTime startDate, LocalDateTime endDate) {
        return auditLogsRepository.filterAuditLogs(userName, operation, startDate, endDate);
    }

    @Override
    public ResponseWrapper<AuditLogsEntity> updateAuditLog(Integer id, AuditLogsdto dto) {
        AuditLogsEntity existing = auditLogsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("AuditLog not found"));

       UsersEntity user = usersRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("AuditLog not found with id: " + dto.getUserId()));

        BeanUtils.copyProperties(dto, existing,"logId"); // Copy all matching fields from DTO
        existing.setUser(user);
        //existing.setUpdatedAt(LocalDateTime.now());

        AuditLogsEntity updated = auditLogsRepository.save(existing);
        return new ResponseWrapper<>("audiLog updated successfully", updated);
    }


    @Override
    public ResponseWrapper<String> deleteAuditLog(Integer id) {
        AuditLogsEntity auditLogs = auditLogsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("AuditLog not found"));

        auditLogs.setDeleted(true);  // Mark as deleted
        auditLogsRepository.save(auditLogs); // Save the updated project

        return new ResponseWrapper<>("AuditLogs deleted successfully",null);
    }
}
