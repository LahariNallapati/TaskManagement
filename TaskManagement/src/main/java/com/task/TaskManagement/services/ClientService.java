package com.task.TaskManagement.services;

import com.task.TaskManagement.Entity.ClientEntity;
import com.task.TaskManagement.Entity.UsersEntity;
import com.task.TaskManagement.dto.Clientdto;
import com.task.TaskManagement.dto.ResponseWrapper;
import com.task.TaskManagement.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface ClientService {

    ResponseWrapper<ClientEntity> createClient(Clientdto dto);

    ResponseWrapper<ClientEntity> updateClient(Integer id, Clientdto dto);

    //ResponseWrapper<String> deleteClient(Integer id);
    ResponseWrapper<String> deleteClient(Integer id);

    ResponseWrapper<ClientEntity> getClientById(Integer id);
   ResponseWrapper<List<ClientEntity>> searchClients(String name, Integer pageNo, Integer pageSize);

    ResponseWrapper<List<ClientEntity>> getAllClients();


}
