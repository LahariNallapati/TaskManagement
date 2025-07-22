package com.task.TaskManagement.services;

import com.task.TaskManagement.Entity.ProjectsEntity;
import com.task.TaskManagement.Entity.TasksEntity;
import com.task.TaskManagement.dto.ProjectsDto;
import com.task.TaskManagement.dto.ResponseWrapper;
import com.task.TaskManagement.dto.TasksDto;

import java.util.List;

public interface TasksService {
    ResponseWrapper<TasksEntity> createTask(TasksDto dto);

    ResponseWrapper<TasksEntity> updateTask(Integer id, TasksDto dto);

    //ResponseWrapper<String> deleteProject(Integer id);
    ResponseWrapper<String> deleteTask(Integer id);

    ResponseWrapper<TasksEntity> getTaskById(Integer id);

    ResponseWrapper<List<TasksEntity>> getAllTasks();

    ResponseWrapper<List<TasksEntity>> searchTasks(String name, Integer pageNo, Integer pageSize);

    List<TasksEntity> getTasksPageOnly(int pageNo, int pageSize);

    ResponseWrapper<TasksDto> markTaskAsComplete(Integer taskId);

    List<TasksEntity> filterTasks(String projectName, String status);

}
