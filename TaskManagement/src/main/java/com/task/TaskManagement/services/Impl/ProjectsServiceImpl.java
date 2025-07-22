package com.task.TaskManagement.services.Impl;

import com.task.TaskManagement.Entity.ClientEntity;
import com.task.TaskManagement.Entity.ProjectsEntity;
import com.task.TaskManagement.Entity.UsersEntity;
import com.task.TaskManagement.dao.ClientRepository;
import com.task.TaskManagement.dao.ProjectsRepository;
import com.task.TaskManagement.dto.ProjectsDto;
import com.task.TaskManagement.dto.ResponseWrapper;
import com.task.TaskManagement.dto.UserDto;
import com.task.TaskManagement.services.ProjectsService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectsServiceImpl implements ProjectsService {
    @Autowired
    private ProjectsRepository projectRepository;
    @Autowired
    private ClientRepository clientRepository;


    @Override
    public ResponseWrapper<ProjectsEntity> createProject(ProjectsDto dto) {
        ClientEntity client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + dto.getClientId()));

        ProjectsEntity project = new ProjectsEntity();
        BeanUtils.copyProperties(dto, project); // Automatically maps matching fields
        project.setClient(client);
        project.setCreatedAt(LocalDateTime.now());
        project.setUpdatedAt(LocalDateTime.now());

        ProjectsEntity saved = projectRepository.save(project);
        return new ResponseWrapper<>("Project created successfully", saved);
    }

    @Override
    public ResponseWrapper<ProjectsEntity> updateProject(Integer id, ProjectsDto dto) {
        ProjectsEntity existing = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));

        ClientEntity client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + dto.getClientId()));

        BeanUtils.copyProperties(dto, existing,"projectId"); // Copy all matching fields from DTO
        existing.setClient(client);
        existing.setUpdatedAt(LocalDateTime.now());

        ProjectsEntity updated = projectRepository.save(existing);
        return new ResponseWrapper<>("Project updated successfully", null);
    }

    @Override
    public ResponseWrapper<String> deleteProject(Integer id) {
        ProjectsEntity project = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));

        project.setIsDeleted(true);  // Mark as deleted
        projectRepository.save(project); // Save the updated project

        return new ResponseWrapper<>("Project deleted successfully",null);
    }


    @Override
    public ResponseWrapper<ProjectsEntity> getProjectById(Integer id) {
        ProjectsEntity project = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));
        return new ResponseWrapper<>("Project fetched successfully", project);
    }

    @Override
    public ResponseWrapper<List<ProjectsEntity>> getAllProjects() {
        List<ProjectsEntity> list = projectRepository.findAll();
        return new ResponseWrapper<>("All users fetched successfully", list);
    }

    @Override
    public ResponseWrapper<List<ProjectsEntity>> searchProjects(String name, Integer pageNo, Integer pageSize) {
        List<ProjectsEntity> project = new ArrayList<>();
        PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize);
        List<ProjectsEntity> taskEntities = projectRepository.searchprojects(name, pageRequest);

        project.forEach(projectEntity -> {
            ProjectsDto projects = new ProjectsDto();
            BeanUtils.copyProperties(projectEntity, projects);
            project.add(projectEntity);
        });

        return new ResponseWrapper<>("Project Detailes fetched successfully", project);
    }


    @Override
    public List<ProjectsEntity> getProjectsPageOnly(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<ProjectsEntity> page = projectRepository.findAll(pageable);
        return page.getContent();
    }

    @Override
    public List<ProjectsEntity> filterProjectsByClientNameAndPriority(String clientName, String priority) {
        return projectRepository.filterProjectsByClientNameAndPriority(clientName, priority);
    }
}

