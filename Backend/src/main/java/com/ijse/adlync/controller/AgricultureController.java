package com.ijse.adlync.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.ijse.adlync.dto.request.AgricultureRequestDTO;
import com.ijse.adlync.dto.response.AgricultureResponseDTO;
import com.ijse.adlync.service.AgricultureServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/agricultures")
@Tag(name = "AgricultureEntity Management", description = "APIs for managing AgricultureEntity entities")
public class AgricultureController {

    @Autowired
    private AgricultureServiceImpl service;

    @GetMapping
    @Operation(summary = "Get all Agricultures", description = "Retrieve a list of all Agriculture entities")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of Agricultures"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<AgricultureResponseDTO>> getAllAgricultures() {
        List<AgricultureResponseDTO> response = service.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Agriculture by ID", description = "Retrieve a Agriculture entity by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved Agriculture"),
        @ApiResponse(responseCode = "404", description = "Agriculture not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<AgricultureResponseDTO> getAgricultureById(@Parameter(description = "ID of the Agriculture to retrieve") @PathVariable Long id) {
        AgricultureResponseDTO response = service.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create new Agriculture", description = "Create a new Agriculture entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created Agriculture"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<AgricultureResponseDTO> createAgriculture(@Parameter(description = "Agriculture data to create") @RequestBody AgricultureRequestDTO requestDTO) {
        AgricultureResponseDTO response = service.create(requestDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Agriculture", description = "Update an existing Agriculture entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated Agriculture"),
        @ApiResponse(responseCode = "404", description = "Agriculture not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<AgricultureResponseDTO> updateAgriculture(@Parameter(description = "ID of the Agriculture to update") @PathVariable Long id, @Parameter(description = "Updated Agriculture data") @RequestBody AgricultureRequestDTO requestDTO) {
        AgricultureResponseDTO response = service.update(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Agriculture", description = "Delete a Agriculture entity by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted Agriculture"),
        @ApiResponse(responseCode = "404", description = "Agriculture not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteAgriculture(@Parameter(description = "ID of the Agriculture to delete") @PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
