package com.task.TaskManagement.services;


import com.task.TaskManagement.Entity.UsersEntity;

import com.task.TaskManagement.dto.LoginRequest;
import com.task.TaskManagement.dto.LoginResponse;
import com.task.TaskManagement.dto.ResponseWrapper;
import com.task.TaskManagement.dto.UserDto;

import java.util.List;

public interface UserService {


    ResponseWrapper<UsersEntity> createUser(UserDto dto);

    ResponseWrapper<UsersEntity> updateUser(Integer id, UserDto dto);

    //ResponseWrapper<String> deleteClient(Integer id);
    ResponseWrapper<String> deleteUser(Integer id);

    ResponseWrapper<UsersEntity> getUserById(Integer id);

    ResponseWrapper<List<UsersEntity>> getAllUsers();

    LoginResponse login(LoginRequest request);
}