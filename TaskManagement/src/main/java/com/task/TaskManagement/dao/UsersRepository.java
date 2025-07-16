package com.task.TaskManagement.dao;

import com.task.TaskManagement.Entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<UsersEntity,Integer> {
    Optional<UsersEntity> findByEmail(String email);
}
