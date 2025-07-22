package com.task.TaskManagement.dao;

import com.task.TaskManagement.Entity.ProjectsEntity;
import com.task.TaskManagement.Entity.UsersEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectsRepository extends JpaRepository<ProjectsEntity,Integer>{
    @Query("SELECT s FROM ProjectsEntity s WHERE LOWER(s.projectName) LIKE LOWER(CONCAT('%', :projectName, '%'))")
    List<ProjectsEntity> searchprojects(@Param(value = "projectName") String name, PageRequest pageRequest);

    // Filter by clientId and priority (both optional)
    @Query("SELECT p FROM ProjectsEntity p WHERE "
            + "(:clientName IS NULL OR LOWER(p.client.clientName) LIKE LOWER(CONCAT('%', :clientName, '%'))) AND "
            + "(:priority IS NULL OR LOWER(p.priority) = LOWER(:priority))")
    List<ProjectsEntity> filterProjectsByClientNameAndPriority(
            @Param("clientName") String clientName,
            @Param("priority") String priority
    );

//    List<ProjectsEntity> filterProjectsByClientNameAndPriority(String clientName, String priority);
}
