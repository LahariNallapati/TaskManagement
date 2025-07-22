package com.task.TaskManagement.controllers;

import com.task.TaskManagement.Entity.AuditLogsEntity;
import com.task.TaskManagement.Entity.ClientEntity;
import com.task.TaskManagement.Entity.UsersEntity;
import com.task.TaskManagement.annotations.AdminOnly;
import com.task.TaskManagement.dto.AuditLogsdto;
import com.task.TaskManagement.dto.Clientdto;
import com.task.TaskManagement.dto.ResponseWrapper;
import com.task.TaskManagement.services.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping
    @AdminOnly
    public ResponseEntity<ResponseWrapper<ClientEntity>> createClient( @RequestBody Clientdto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.createClient(dto));
    }

    @PutMapping("/{id}")
    @AdminOnly
    public ResponseEntity<ResponseWrapper<ClientEntity>> updateClients(@PathVariable Integer id, @RequestBody Clientdto dto) {
        return ResponseEntity.ok(clientService.updateClient(id, dto));
    }
    @DeleteMapping("/{id}")
    @AdminOnly
    public ResponseEntity<ResponseWrapper<String>> deleteClients(@PathVariable Integer id) {
        return ResponseEntity.ok(clientService.deleteClient(id));
    }

    @GetMapping("/{id}")
    @AdminOnly
    public ResponseEntity<ResponseWrapper<ClientEntity>> getClientById(@PathVariable Integer id) {
        return ResponseEntity.ok(clientService.getClientById(id));
    }
    @GetMapping
    @AdminOnly
    public ResponseEntity<ResponseWrapper<List<ClientEntity>>> getAllclients () {

        return ResponseEntity.ok(clientService.getAllClients());
    }

    @GetMapping("search")
    @AdminOnly
    public ResponseEntity<ResponseWrapper<List<ClientEntity>>> searchClients(@RequestParam String name, @RequestParam(required = false) Integer pageNo,
                                                        @RequestParam(required = false) Integer pageSize) {
//
//        if (pageNo == null || pageSize == null) {
//            pageNo = 1;
//            pageSize = 10;
//        }

        if (pageNo < 1 || pageNo > 10) {
            throw new IllegalArgumentException("Page number must be between 1 and 10");
        }
        if (pageSize < 1 || pageSize > 100) {
            throw new IllegalArgumentException("Page size must be between 1 and 100");
        }


        return  ResponseEntity.ok(clientService.searchClients(name, pageNo, pageSize));
    }
    @GetMapping("/paged")
    public ResponseEntity<ResponseWrapper<List<ClientEntity>>> getClientByPage(
            @RequestParam(defaultValue = "1") int pageNo) {

        int pageSize = 5;
        List<ClientEntity> client = clientService.getClientPageOnly(pageNo - 1, pageSize); // Page index is 0-based

        return ResponseEntity.ok(new ResponseWrapper<>("Client fetched successfully", client));
    }

}
