package com.task.TaskManagement.services.Impl;

import com.task.TaskManagement.Entity.*;
import com.task.TaskManagement.dao.AllocationsRepository;
import com.task.TaskManagement.dao.ProjectsRepository;
import com.task.TaskManagement.dao.TasksRepository;
import com.task.TaskManagement.dao.UsersRepository;
import com.task.TaskManagement.dto.Allocationdto;
import com.task.TaskManagement.dto.ResponseWrapper;
import com.task.TaskManagement.services.AllocationsService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AllocationsServiceImpl implements AllocationsService {

    @Autowired
    private AllocationsRepository allocationsRepository;

    @Autowired
    private ProjectsRepository projectsRepository;

    @Autowired
    private TasksRepository tasksRepository;

    @Autowired
    private UsersRepository usersRepository;


    @Override
    public ResponseWrapper<AllocationsEntity> createAllocation(Allocationdto dto) {
        UsersEntity users = usersRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getUserId()));

        ProjectsEntity projects = projectsRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getProjectId()));

        TasksEntity tasks = tasksRepository.findById(dto.getTaskId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getTaskId()));


        AllocationsEntity allocations = new AllocationsEntity();
        BeanUtils.copyProperties(dto, allocations, "allocationId"); // Automatically maps matching fields
        allocations.setUser(users);
        allocations.setProject(projects);
        allocations.setTask(tasks);
//        project.setCreatedAt(LocalDateTime.now());
//        project.setUpdatedAt(LocalDateTime.now());

        AllocationsEntity saved = allocationsRepository.save(allocations);
        return new ResponseWrapper<>("Allocations created successfully", saved);
    }

    @Override
    public ResponseWrapper<AllocationsEntity> getAllocationById(Integer id) {
        AllocationsEntity allocations = allocationsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Allocations not found"));
         return new ResponseWrapper<>("Allocation fetched successfully", allocations);
    }


    @Override
    public ResponseWrapper<List<AllocationsEntity>> getAllAllocations() {
        List<AllocationsEntity> list = allocationsRepository.findAll();
        return new ResponseWrapper<>("All clients fetched successfully", list);
    }

    @Override
    public List<AllocationsEntity> getAllocationsPageOnly(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<AllocationsEntity> task = allocationsRepository.findAll(pageable);
        return task.getContent();
    }

    @Override
    public ResponseWrapper<AllocationsEntity> updateAllocation(Integer id, Allocationdto dto) {
        AllocationsEntity existing = allocationsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Allocation not found"));

        UsersEntity users = usersRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getUserId()));

        ProjectsEntity projects = projectsRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + dto.getProjectId()));

        TasksEntity tasks = tasksRepository.findById(dto.getTaskId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getTaskId()));

        BeanUtils.copyProperties(dto, existing,"allocationId"); // Copy all matching fields from DTO

        existing.setUser(users);
        existing.setProject(projects);
        existing.setTask(tasks);
        //existing.setUpdatedAt(LocalDateTime.now());

        AllocationsEntity saved = allocationsRepository.save(existing);
        return new ResponseWrapper<>("Allocations updated successfully", null);
    }

    @Override
    public ResponseWrapper<String> deleteAllocation(Integer id) {
       AllocationsEntity allocations = allocationsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));

        allocations.setDeleted(true);
        allocationsRepository.save(allocations); // Save the updated project

        return new ResponseWrapper<>("Client deleted successfully",null);
    }
}
