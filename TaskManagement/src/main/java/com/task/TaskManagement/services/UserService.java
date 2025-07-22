package com.task.TaskManagement.services;


import com.task.TaskManagement.Entity.ClientEntity;
import com.task.TaskManagement.Entity.UsersEntity;

import com.task.TaskManagement.dto.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {


    ResponseWrapper<UsersEntity> createUser(UserDto dto);

    ResponseWrapper<UsersEntity> updateUser(Integer id, UserDto dto);

    //ResponseWrapper<String> deleteClient(Integer id);
    ResponseWrapper<String> deleteUser(Integer id);

    ResponseWrapper<UsersEntity> getUserById(Integer id);

    ResponseWrapper<List<UsersEntity>> getAllUsers();

    LoginResponse login(LoginRequest request);
    void logout(String token);
    void changePassword(String email, ChangePassword request);
    void sendResetLink(String email);
    void resetPassword(ResetPasswordRequest request);
    ResponseWrapper<List<UsersEntity>> searchUsers(String name, Integer pageNo, Integer pageSize);
    List<UsersEntity> getUsersPageOnly(int pageNo, int pageSize);
    ResponseWrapper<String> deactivateUser(Integer id);
   // LoginResponse login(LoginRequest request);
   List<UsersEntity> getUsersByActiveStatus(Boolean active);
}