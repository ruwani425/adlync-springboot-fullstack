package com.ijse.adlync.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.ijse.adlync.dto.request.Work_over_seasRequestDTO;
import com.ijse.adlync.dto.response.Work_over_seasResponseDTO;
import com.ijse.adlync.service.Work_over_seasServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/work_over_seass")
@Tag(name = "Work_over_seasEntity Management", description = "APIs for managing Work_over_seasEntity entities")
public class Work_over_seasController {

    @Autowired
    private Work_over_seasServiceImpl service;

    @GetMapping
    @Operation(summary = "Get all Work_over_seass", description = "Retrieve a list of all Work_over_seas entities")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of Work_over_seass"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<Work_over_seasResponseDTO>> getAllWork_over_seass() {
        List<Work_over_seasResponseDTO> response = service.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Work_over_seas by ID", description = "Retrieve a Work_over_seas entity by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved Work_over_seas"),
        @ApiResponse(responseCode = "404", description = "Work_over_seas not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Work_over_seasResponseDTO> getWork_over_seasById(@Parameter(description = "ID of the Work_over_seas to retrieve") @PathVariable Long id) {
        Work_over_seasResponseDTO response = service.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create new Work_over_seas", description = "Create a new Work_over_seas entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created Work_over_seas"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Work_over_seasResponseDTO> createWork_over_seas(@Parameter(description = "Work_over_seas data to create") @RequestBody Work_over_seasRequestDTO requestDTO) {
        Work_over_seasResponseDTO response = service.create(requestDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Work_over_seas", description = "Update an existing Work_over_seas entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated Work_over_seas"),
        @ApiResponse(responseCode = "404", description = "Work_over_seas not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Work_over_seasResponseDTO> updateWork_over_seas(@Parameter(description = "ID of the Work_over_seas to update") @PathVariable Long id, @Parameter(description = "Updated Work_over_seas data") @RequestBody Work_over_seasRequestDTO requestDTO) {
        Work_over_seasResponseDTO response = service.update(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Work_over_seas", description = "Delete a Work_over_seas entity by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted Work_over_seas"),
        @ApiResponse(responseCode = "404", description = "Work_over_seas not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteWork_over_seas(@Parameter(description = "ID of the Work_over_seas to delete") @PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
