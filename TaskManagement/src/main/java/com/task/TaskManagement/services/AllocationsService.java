package com.task.TaskManagement.services;

import com.task.TaskManagement.Entity.AllocationsEntity;
import com.task.TaskManagement.Entity.ProjectsEntity;
import com.task.TaskManagement.dto.Allocationdto;
import com.task.TaskManagement.dto.ResponseWrapper;

import java.util.List;

public interface AllocationsService {

    ResponseWrapper<AllocationsEntity> createAllocation(Allocationdto dto);
    ResponseWrapper<AllocationsEntity> updateAllocation(Integer id, Allocationdto dto);
    //ResponseWrapper<String> deleteProject(Integer id);
    ResponseWrapper<String> deleteAllocation(Integer id);
    ResponseWrapper<AllocationsEntity> getAllocationById(Integer id);
    ResponseWrapper<List<AllocationsEntity>> getAllAllocations();
    List<AllocationsEntity> getAllocationsPageOnly(int pageNo, int pageSize);
}
