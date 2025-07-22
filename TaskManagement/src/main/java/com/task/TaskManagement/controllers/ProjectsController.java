package com.task.TaskManagement.controllers;

import com.task.TaskManagement.Entity.ProjectsEntity;
import com.task.TaskManagement.Entity.UsersEntity;
import com.task.TaskManagement.annotations.AdminOnly;
import com.task.TaskManagement.dto.ProjectsDto;
import com.task.TaskManagement.dto.ResponseWrapper;
import com.task.TaskManagement.services.ProjectsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectsController {

    @Autowired
    private ProjectsService projectService;

    @PostMapping
    public ResponseEntity<ResponseWrapper<ProjectsEntity>> createProject(@Valid @RequestBody ProjectsDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(projectService.createProject(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseWrapper<ProjectsEntity>> updateProject(@PathVariable Integer id, @RequestBody ProjectsDto dto) {
        return ResponseEntity.ok(projectService.updateProject(id, dto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseWrapper<String>> deleteProject(@PathVariable Integer id) {
        return ResponseEntity.ok(projectService.deleteProject(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper<ProjectsEntity>> getProjectById(@PathVariable Integer id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper<List<ProjectsEntity>>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }
    @GetMapping("/paged")
    public ResponseEntity<ResponseWrapper<List<ProjectsEntity>>> getProjectByPage(
            @RequestParam(defaultValue = "1") int pageNo) {

        int pageSize = 5;
        List<ProjectsEntity> projects = projectService.getProjectsPageOnly(pageNo - 1, pageSize); // Page index is 0-based

        return ResponseEntity.ok(new ResponseWrapper<>("Projects fetched successfully", projects));
    }

    @GetMapping("search")
    @AdminOnly
    public ResponseEntity<ResponseWrapper<List<ProjectsEntity>>> searchProjects(@RequestParam String name, @RequestParam(required = false) Integer pageNo,
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


        return  ResponseEntity.ok(projectService.searchProjects(name, pageNo, pageSize));
    }
    @GetMapping("/filter")
    public ResponseEntity<List<ProjectsEntity>> filterProjects(
            @RequestParam(required = false) String clientName,
            @RequestParam(required = false) String priority) {

        List<ProjectsEntity> filteredProjects = projectService
                .filterProjectsByClientNameAndPriority(clientName, priority);

        return ResponseEntity.ok(filteredProjects);
    }

}

