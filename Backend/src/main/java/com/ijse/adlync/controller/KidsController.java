package com.ijse.adlync.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.ijse.adlync.dto.request.KidsRequestDTO;
import com.ijse.adlync.dto.response.KidsResponseDTO;
import com.ijse.adlync.service.impl.KidsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/kidss")
@Tag(name = "KidsEntity Management", description = "APIs for managing KidsEntity entities")
public class KidsController {

    @Autowired
    private KidsServiceImpl service;

    @GetMapping
    @Operation(summary = "Get all Kidss", description = "Retrieve a list of all Kids entities")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of Kidss"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<KidsResponseDTO>> getAllKidss() {
        List<KidsResponseDTO> response = service.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Kids by ID", description = "Retrieve a Kids entity by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved Kids"),
        @ApiResponse(responseCode = "404", description = "Kids not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<KidsResponseDTO> getKidsById(@Parameter(description = "ID of the Kids to retrieve") @PathVariable Long id) {
        KidsResponseDTO response = service.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create new Kids", description = "Create a new Kids entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created Kids"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<KidsResponseDTO> createKids(@Parameter(description = "Kids data to create") @RequestBody KidsRequestDTO requestDTO) {
        KidsResponseDTO response = service.create(requestDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Kids", description = "Update an existing Kids entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated Kids"),
        @ApiResponse(responseCode = "404", description = "Kids not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<KidsResponseDTO> updateKids(@Parameter(description = "ID of the Kids to update") @PathVariable Long id, @Parameter(description = "Updated Kids data") @RequestBody KidsRequestDTO requestDTO) {
        KidsResponseDTO response = service.update(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Kids", description = "Delete a Kids entity by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted Kids"),
        @ApiResponse(responseCode = "404", description = "Kids not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteKids(@Parameter(description = "ID of the Kids to delete") @PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
