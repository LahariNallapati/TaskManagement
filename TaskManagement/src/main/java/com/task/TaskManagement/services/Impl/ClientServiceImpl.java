package com.task.TaskManagement.services.Impl;

import com.task.TaskManagement.Entity.AuditLogsEntity;
import com.task.TaskManagement.Entity.ClientEntity;
import com.task.TaskManagement.Entity.UsersEntity;
import com.task.TaskManagement.dao.ClientRepository;
import com.task.TaskManagement.dto.Clientdto;
import com.task.TaskManagement.dto.ResponseWrapper;
import com.task.TaskManagement.dto.UserDto;
import com.task.TaskManagement.services.ClientService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public ResponseWrapper<ClientEntity> createClient(Clientdto dto) {
       ClientEntity client = new ClientEntity();
        BeanUtils.copyProperties(dto, client, "clientId");
//        client.setPassword(passwordEncoder.encode(dto.getPassword()));// Automatically maps matching fields
        client.setUpdatedAt(LocalDateTime.now());
        ClientEntity saved = clientRepository.save(client);
        return new ResponseWrapper<>("client created successfully", saved);
    }

    @Override
    public ResponseWrapper<List<ClientEntity>> getAllClients() {
        List<ClientEntity> list =clientRepository.findAll();
        return new ResponseWrapper<>("All users fetched successfully", list);
    }

    @Override
    public ResponseWrapper<ClientEntity> getClientById(Integer id) {
        ClientEntity client = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        return new ResponseWrapper<>("Client fetched successfully", client);
    }

    @Override
    public ResponseWrapper<ClientEntity> updateClient(Integer id, Clientdto dto) {
        ClientEntity existing = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));



        BeanUtils.copyProperties(dto, existing,"logId"); // Copy all matching fields from DTO
        //existing.setUser(user);
        existing.setUpdatedAt(LocalDateTime.now());

        ClientEntity updated = clientRepository.save(existing);
        return new ResponseWrapper<>("Client updated successfully", updated);
    }

    @Override
    public ResponseWrapper<String> deleteClient(Integer id) {
       ClientEntity client = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        client.setDeleted(true);  // Mark as deleted
       clientRepository.save(client); // Save the updated project

        return new ResponseWrapper<>("Client deleted successfully",null);
    }

    @Override
    public ResponseWrapper<List<ClientEntity>> searchClients(String name, Integer pageNo, Integer pageSize) {
        List<ClientEntity> client = new ArrayList<>();
        PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize);
        List<ClientEntity> clientEntities = clientRepository.searchClients(name, pageRequest);

        client.forEach(clientEntity -> {
            Clientdto clients = new Clientdto();
            BeanUtils.copyProperties(clientEntity, clients);
            client.add(clientEntity);
        });

        return new ResponseWrapper<>("All users fetched successfully", client);

    }
}
