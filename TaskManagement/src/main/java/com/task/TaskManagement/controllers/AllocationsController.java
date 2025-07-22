package com.task.TaskManagement.controllers;

import com.task.TaskManagement.Entity.AllocationsEntity;
import com.task.TaskManagement.Entity.ClientEntity;
import com.task.TaskManagement.dto.Allocationdto;
import com.task.TaskManagement.dto.Clientdto;
import com.task.TaskManagement.dto.ResponseWrapper;
import com.task.TaskManagement.services.AllocationsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/allocations")
public class AllocationsController {

    @Autowired
    private AllocationsService allocationsService;


//@PostMapping
//public ResponseEntity<AllocationsEntity> create(@RequestBody AllocationsEntity allocationsEntity) {
//    return ResponseEntity.ok(allocationsService.save(allocationsEntity));
//}
//
//    @GetMapping
//    public ResponseEntity<List<AllocationsEntity>> getAll(){
//        return ResponseEntity.ok(allocationsService.getAll());
//    }
//
//
//    @PutMapping("/{allocationId}")
//    public ResponseEntity<AllocationsEntity> update(@PathVariable int allocationId, @RequestBody AllocationsEntity allocationsEntity){
//        return ResponseEntity.ok(allocationsService.update(allocationId, allocationsEntity));
//    }
//
//    @DeleteMapping("/{allocationId}")
//    public ResponseEntity<String> delete(@PathVariable int allocationId){
//        allocationsService.delete(allocationId);
//        return ResponseEntity.ok("Allocations deleted with Id: " + allocationId);
//    }


@PostMapping
public ResponseEntity<ResponseWrapper<AllocationsEntity>> createAllocation(@Valid @RequestBody Allocationdto dto) {
    return ResponseEntity.status(HttpStatus.OK).body(allocationsService.createAllocation(dto));
}

    @PutMapping("/{id}")
    public ResponseEntity<ResponseWrapper<AllocationsEntity>> updateAllocation(@PathVariable Integer id, @RequestBody Allocationdto dto) {
        return ResponseEntity.ok(allocationsService.updateAllocation(id, dto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseWrapper<String>> deleteAllocation(@PathVariable Integer id) {
        return ResponseEntity.ok(allocationsService.deleteAllocation(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper<AllocationsEntity>> getAllocantioById(@PathVariable Integer id) {
        return ResponseEntity.ok(allocationsService.getAllocationById(id));
    }
        @GetMapping
        public ResponseEntity<ResponseWrapper<List<AllocationsEntity>>> getAllAllocation () {

            return ResponseEntity.ok(allocationsService.getAllAllocations());
        }
    @GetMapping("/paged")
    public ResponseEntity<ResponseWrapper<List<AllocationsEntity>>> getAllocationsByPage(
            @RequestParam(defaultValue = "1") int pageNo) {

        int pageSize = 5;
        List<AllocationsEntity> allocations = allocationsService.getAllocationsPageOnly(pageNo - 1, pageSize); // Page index is 0-based

        return ResponseEntity.ok(new ResponseWrapper<>("Allocations fetched successfully", allocations));
    }

}
