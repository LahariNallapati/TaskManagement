package com.task.TaskManagement.dao;

import com.task.TaskManagement.Entity.ClientEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClientRepository extends JpaRepository<ClientEntity, Integer> {
    @Query("SELECT s FROM ClientEntity s WHERE LOWER(s.clientName) LIKE LOWER(CONCAT('%', :clientName, '%'))")
    List<ClientEntity> searchClients(@Param(value = "clientName") String name, PageRequest pageRequest);

}
