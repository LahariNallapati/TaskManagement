package com.task.TaskManagement.dao;

import com.task.TaskManagement.Entity.ProjectsEntity;
import com.task.TaskManagement.Entity.TasksEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TasksRepository extends JpaRepository<TasksEntity,Integer> {
    @Query("SELECT s FROM TasksEntity s WHERE LOWER(s.taskName) LIKE LOWER(CONCAT('%', :taskName, '%'))")
    List<TasksEntity> searchtasks(@Param(value = "taskName") String name, PageRequest pageRequest);



    @Query("SELECT t FROM TasksEntity t WHERE "
            + "(:projectName IS NULL OR LOWER(t.project.projectName) LIKE LOWER(CONCAT('%', :projectName, '%'))) AND "
            + "(:status IS NULL OR LOWER(t.status) = LOWER(:status))")
    List<TasksEntity> filterTasks(
            @Param("projectName") String projectName,
            @Param("status") String status
    );

}
