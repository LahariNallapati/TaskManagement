package com.task.TaskManagement.dao;

import com.task.TaskManagement.Entity.ClientEntity;
import com.task.TaskManagement.Entity.UsersEntity;
import com.task.TaskManagement.dto.UserDto;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<UsersEntity,Integer> {
    Optional<UsersEntity> findByEmail(String email);
    @Query("SELECT s FROM UsersEntity s WHERE LOWER(s.userName) LIKE LOWER(CONCAT('%', :userName, '%'))")
    List<UsersEntity> searchUsers(@Param(value = "userName") String name, PageRequest pageRequest);
//    @Query("SELECT u FROM UsersEntity u WHERE LOWER(u.userName) LIKE LOWER(CONCAT('%', :userName, '%'))")
//    Page<UsersEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);
//    @Query("SELECT u FROM UsersEntity u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))")
//    Page<UsersEntity> searchByName(@Param("name") String name, Pageable pageable);


    @Modifying
    @Transactional
    @Query("UPDATE UsersEntity u SET u.active = false WHERE u.userId = :userId")
    int deactivateUser(@Param("userId") Integer userId);

  //  List<UserDto> getUsersByActiveStatus(Boolean active);

    List<UsersEntity> findByActive(Boolean active);
//List<UsersEntity> findByActive(Boolean active);

}

