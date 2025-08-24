package com.ijse.adlync.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.ijse.adlync.dto.request.JobRequestDTO;
import com.ijse.adlync.dto.response.JobResponseDTO;
import com.ijse.adlync.service.JobServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/jobs")
@Tag(name = "JobEntity Management", description = "APIs for managing JobEntity entities")
public class JobController {

    @Autowired
    private JobServiceImpl service;

    @GetMapping
    @Operation(summary = "Get all Jobs", description = "Retrieve a list of all Job entities")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of Jobs"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<JobResponseDTO>> getAllJobs() {
        List<JobResponseDTO> response = service.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Job by ID", description = "Retrieve a Job entity by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved Job"),
        @ApiResponse(responseCode = "404", description = "Job not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<JobResponseDTO> getJobById(@Parameter(description = "ID of the Job to retrieve") @PathVariable Long id) {
        JobResponseDTO response = service.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create new Job", description = "Create a new Job entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created Job"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<JobResponseDTO> createJob(@Parameter(description = "Job data to create") @RequestBody JobRequestDTO requestDTO) {
        JobResponseDTO response = service.create(requestDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Job", description = "Update an existing Job entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated Job"),
        @ApiResponse(responseCode = "404", description = "Job not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<JobResponseDTO> updateJob(@Parameter(description = "ID of the Job to update") @PathVariable Long id, @Parameter(description = "Updated Job data") @RequestBody JobRequestDTO requestDTO) {
        JobResponseDTO response = service.update(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Job", description = "Delete a Job entity by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted Job"),
        @ApiResponse(responseCode = "404", description = "Job not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteJob(@Parameter(description = "ID of the Job to delete") @PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
