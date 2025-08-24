package com.ijse.adlync.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.ijse.adlync.dto.request.ElectronicRequestDTO;
import com.ijse.adlync.dto.response.ElectronicResponseDTO;
import com.ijse.adlync.service.ElectronicServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/electronics")
@Tag(name = "ElectronicEntity Management", description = "APIs for managing ElectronicEntity entities")
public class ElectronicController {

    @Autowired
    private ElectronicServiceImpl service;

    @GetMapping
    @Operation(summary = "Get all Electronics", description = "Retrieve a list of all Electronic entities")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of Electronics"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<ElectronicResponseDTO>> getAllElectronics() {
        List<ElectronicResponseDTO> response = service.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Electronic by ID", description = "Retrieve a Electronic entity by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved Electronic"),
        @ApiResponse(responseCode = "404", description = "Electronic not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ElectronicResponseDTO> getElectronicById(@Parameter(description = "ID of the Electronic to retrieve") @PathVariable Long id) {
        ElectronicResponseDTO response = service.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create new Electronic", description = "Create a new Electronic entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created Electronic"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ElectronicResponseDTO> createElectronic(@Parameter(description = "Electronic data to create") @RequestBody ElectronicRequestDTO requestDTO) {
        ElectronicResponseDTO response = service.create(requestDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Electronic", description = "Update an existing Electronic entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated Electronic"),
        @ApiResponse(responseCode = "404", description = "Electronic not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ElectronicResponseDTO> updateElectronic(@Parameter(description = "ID of the Electronic to update") @PathVariable Long id, @Parameter(description = "Updated Electronic data") @RequestBody ElectronicRequestDTO requestDTO) {
        ElectronicResponseDTO response = service.update(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Electronic", description = "Delete a Electronic entity by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted Electronic"),
        @ApiResponse(responseCode = "404", description = "Electronic not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteElectronic(@Parameter(description = "ID of the Electronic to delete") @PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
