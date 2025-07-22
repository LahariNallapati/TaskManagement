package com.task.TaskManagement.services;

import com.task.TaskManagement.Entity.ProjectsEntity;
import com.task.TaskManagement.Entity.UsersEntity;
import com.task.TaskManagement.dto.ProjectsDto;
import com.task.TaskManagement.dto.ResponseWrapper;

import java.util.List;

public interface ProjectsService {
    ResponseWrapper<ProjectsEntity> createProject(ProjectsDto dto);
    ResponseWrapper<ProjectsEntity> updateProject(Integer id, ProjectsDto dto);
    //ResponseWrapper<String> deleteProject(Integer id);
    ResponseWrapper<String> deleteProject(Integer id);
    ResponseWrapper<ProjectsEntity> getProjectById(Integer id);
    ResponseWrapper<List<ProjectsEntity>> getAllProjects();
    ResponseWrapper<List<ProjectsEntity>> searchProjects(String name, Integer pageNo, Integer pageSize);
    List<ProjectsEntity> getProjectsPageOnly(int pageNo, int pageSize);
    List<ProjectsEntity> filterProjectsByClientNameAndPriority(String clientName, String priority);

}
