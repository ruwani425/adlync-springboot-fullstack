package com.ijse.adlync.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.ijse.adlync.dto.request.EssentialsRequestDTO;
import com.ijse.adlync.dto.response.EssentialsResponseDTO;
import com.ijse.adlync.service.EssentialsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/essentialss")
@Tag(name = "EssentialsEntity Management", description = "APIs for managing EssentialsEntity entities")
public class EssentialsController {

    @Autowired
    private EssentialsServiceImpl service;

    @GetMapping
    @Operation(summary = "Get all Essentialss", description = "Retrieve a list of all Essentials entities")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of Essentialss"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<EssentialsResponseDTO>> getAllEssentialss() {
        List<EssentialsResponseDTO> response = service.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Essentials by ID", description = "Retrieve a Essentials entity by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved Essentials"),
        @ApiResponse(responseCode = "404", description = "Essentials not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<EssentialsResponseDTO> getEssentialsById(@Parameter(description = "ID of the Essentials to retrieve") @PathVariable Long id) {
        EssentialsResponseDTO response = service.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create new Essentials", description = "Create a new Essentials entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created Essentials"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<EssentialsResponseDTO> createEssentials(@Parameter(description = "Essentials data to create") @RequestBody EssentialsRequestDTO requestDTO) {
        EssentialsResponseDTO response = service.create(requestDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Essentials", description = "Update an existing Essentials entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated Essentials"),
        @ApiResponse(responseCode = "404", description = "Essentials not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<EssentialsResponseDTO> updateEssentials(@Parameter(description = "ID of the Essentials to update") @PathVariable Long id, @Parameter(description = "Updated Essentials data") @RequestBody EssentialsRequestDTO requestDTO) {
        EssentialsResponseDTO response = service.update(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Essentials", description = "Delete a Essentials entity by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted Essentials"),
        @ApiResponse(responseCode = "404", description = "Essentials not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteEssentials(@Parameter(description = "ID of the Essentials to delete") @PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
