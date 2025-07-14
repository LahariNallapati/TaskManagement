package com.task.TaskManagement.controllers;

import com.task.TaskManagement.Entity.AuditLogsEntity;
import com.task.TaskManagement.dto.AuditLogsdto;
import com.task.TaskManagement.dto.ResponseWrapper;
import com.task.TaskManagement.services.AuditLogsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audits")
public class AuditLogsController {

    @Autowired
    private AuditLogsService auditLogs;

    @PostMapping
    public ResponseEntity<ResponseWrapper<AuditLogsEntity>> createAudits(@Valid @RequestBody AuditLogsdto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(auditLogs.createAuditLog(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseWrapper<AuditLogsEntity>> updateAudits(@PathVariable Integer id, @RequestBody AuditLogsdto dto) {
        return ResponseEntity.ok(auditLogs.updateAuditLog(id, dto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseWrapper<String>> deleteAudits(@PathVariable Integer id) {
        return ResponseEntity.ok(auditLogs.deleteAuditLog(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper<AuditLogsEntity>> getAuditById(@PathVariable Integer id) {
        return ResponseEntity.ok(auditLogs.getAuditLogById(id));
    }
    @GetMapping
    public ResponseEntity<ResponseWrapper<List<AuditLogsEntity>>> getAllAudits () {

        return ResponseEntity.ok(auditLogs.getAllAuditLogs());
    }
}
