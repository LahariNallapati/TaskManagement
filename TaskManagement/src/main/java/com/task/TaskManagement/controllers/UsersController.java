package com.task.TaskManagement.controllers;

import com.task.TaskManagement.Entity.ClientEntity;
import com.task.TaskManagement.Entity.UsersEntity;
import com.task.TaskManagement.annotations.AdminOnly;
import com.task.TaskManagement.dto.Clientdto;
import com.task.TaskManagement.dto.ResponseWrapper;
import com.task.TaskManagement.dto.UserDto;
import com.task.TaskManagement.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    private UserService userService;

//    @PostMapping
//    public ResponseEntity<UsersEntity> createUser(@RequestBody UsersEntity user) {
//        return ResponseEntity.ok(userService.save(user));
//    }
//
//    @GetMapping
//    public ResponseEntity<List<UsersEntity>> getAllUsers() {
//        return ResponseEntity.ok(userService.getAll());
//    }
//
//
//    @PutMapping("/{userId}")
//    public ResponseEntity<UsersEntity> update(@PathVariable int userId, @RequestBody UsersEntity user) {
//        return ResponseEntity.ok(userService.update(userId, user));
//    }
//
//    @DeleteMapping("/{userId}")
//    public ResponseEntity<String> delete(@PathVariable int userId) {
//        userService.delete(userId);
//        return ResponseEntity.ok("User deleted with Id: " + userId);
//    }

    @PostMapping("/signup")
    public ResponseEntity<ResponseWrapper<UsersEntity>> createClient(@Valid @RequestBody UserDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.createUser(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseWrapper<UsersEntity>> updateClient(@PathVariable Integer id, @RequestBody UserDto dto) {
        return ResponseEntity.ok(userService.updateUser(id,dto));
    }
   // @AdminOnly
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseWrapper<String>> deleteClient(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper<UsersEntity>> getClientById(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper<List<UsersEntity>>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
    @GetMapping("search")
    @AdminOnly
    public ResponseEntity<ResponseWrapper<List<UsersEntity>>> searchUsers(@RequestParam String name, @RequestParam(required = false) Integer pageNo,
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


        return  ResponseEntity.ok(userService.searchUsers(name, pageNo, pageSize));
    }

    @GetMapping("/paged")
    public ResponseEntity<ResponseWrapper<List<UsersEntity>>> getUsersByPage(
            @RequestParam(defaultValue = "1") int pageNo) {

        int pageSize = 5;
        List<UsersEntity> users = userService.getUsersPageOnly(pageNo - 1, pageSize); // Page index is 0-based

        return ResponseEntity.ok(new ResponseWrapper<>("Users fetched successfully", users));
    }
@AdminOnly
    @PatchMapping("/deactivate/{userId}")
    public ResponseEntity<ResponseWrapper<String>> deactivateUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(userService.deactivateUser(userId));
    }

    @GetMapping("/filter-by-active")
    public ResponseEntity<List<UsersEntity>> getUsersByActiveStatus(@RequestParam(required = false) Boolean active) {
        List<UsersEntity> users = userService.getUsersByActiveStatus(active);
        return ResponseEntity.ok(users);
    }

}
