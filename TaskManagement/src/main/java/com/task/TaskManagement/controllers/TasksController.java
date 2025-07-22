package com.task.TaskManagement.controllers;


import com.task.TaskManagement.Entity.ProjectsEntity;
import com.task.TaskManagement.Entity.TasksEntity;
import com.task.TaskManagement.annotations.AdminOnly;
import com.task.TaskManagement.dto.ProjectsDto;
import com.task.TaskManagement.dto.ResponseWrapper;
import com.task.TaskManagement.dto.TasksDto;
import com.task.TaskManagement.services.TasksService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
    public class TasksController {

@Autowired
private TasksService taskService;
    @PostMapping
    public ResponseEntity<ResponseWrapper<TasksEntity>> createTask(@Valid @RequestBody TasksDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(taskService.createTask(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseWrapper<TasksEntity>> updateTask(@PathVariable Integer id, @RequestBody TasksDto dto) {
        return ResponseEntity.ok(taskService.updateTask(id, dto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseWrapper<String>> deleteTask(@PathVariable Integer id) {
        return ResponseEntity.ok(taskService.deleteTask(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper<TasksEntity>> getProjectById(@PathVariable Integer id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper<List<TasksEntity>>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }
    @GetMapping("/paged")
    public ResponseEntity<ResponseWrapper<List<TasksEntity>>> geTasksByPage(
            @RequestParam(defaultValue = "1") int pageNo) {

        int pageSize = 5;
        List<TasksEntity> tasks = taskService.getTasksPageOnly(pageNo - 1, pageSize); // Page index is 0-based

        return ResponseEntity.ok(new ResponseWrapper<>("Projects fetched successfully", tasks));
    }

    @GetMapping("search")
    @AdminOnly
    public ResponseEntity<ResponseWrapper<List<TasksEntity>>> searchTasks(@RequestParam String name, @RequestParam(required = false) Integer pageNo,
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


        return  ResponseEntity.ok(taskService.searchTasks(name, pageNo, pageSize));
    }
    @PatchMapping("/{id}/complete")
    public ResponseEntity<ResponseWrapper<TasksDto>> markTaskAsComplete(@PathVariable("id") Integer taskId) {
        ResponseWrapper<TasksDto> response = taskService.markTaskAsComplete(taskId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<TasksEntity>> getFilteredTasks(
            @RequestParam(required = false) String projectName,
            @RequestParam(required = false) String status) {
        List<TasksEntity> tasks = taskService.filterTasks(projectName, status);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

}


