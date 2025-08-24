package com.ijse.adlync.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.ijse.adlync.dto.request.Fashion_and_beautyRequestDTO;
import com.ijse.adlync.dto.response.Fashion_and_beautyResponseDTO;
import com.ijse.adlync.service.Fashion_and_beautyServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/fashion_and_beautys")
@Tag(name = "Fashion_and_beautyEntity Management", description = "APIs for managing Fashion_and_beautyEntity entities")
public class Fashion_and_beautyController {

    @Autowired
    private Fashion_and_beautyServiceImpl service;

    @GetMapping
    @Operation(summary = "Get all Fashion_and_beautys", description = "Retrieve a list of all Fashion_and_beauty entities")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of Fashion_and_beautys"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<Fashion_and_beautyResponseDTO>> getAllFashion_and_beautys() {
        List<Fashion_and_beautyResponseDTO> response = service.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Fashion_and_beauty by ID", description = "Retrieve a Fashion_and_beauty entity by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved Fashion_and_beauty"),
        @ApiResponse(responseCode = "404", description = "Fashion_and_beauty not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Fashion_and_beautyResponseDTO> getFashion_and_beautyById(@Parameter(description = "ID of the Fashion_and_beauty to retrieve") @PathVariable Long id) {
        Fashion_and_beautyResponseDTO response = service.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create new Fashion_and_beauty", description = "Create a new Fashion_and_beauty entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created Fashion_and_beauty"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Fashion_and_beautyResponseDTO> createFashion_and_beauty(@Parameter(description = "Fashion_and_beauty data to create") @RequestBody Fashion_and_beautyRequestDTO requestDTO) {
        Fashion_and_beautyResponseDTO response = service.create(requestDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Fashion_and_beauty", description = "Update an existing Fashion_and_beauty entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated Fashion_and_beauty"),
        @ApiResponse(responseCode = "404", description = "Fashion_and_beauty not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Fashion_and_beautyResponseDTO> updateFashion_and_beauty(@Parameter(description = "ID of the Fashion_and_beauty to update") @PathVariable Long id, @Parameter(description = "Updated Fashion_and_beauty data") @RequestBody Fashion_and_beautyRequestDTO requestDTO) {
        Fashion_and_beautyResponseDTO response = service.update(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Fashion_and_beauty", description = "Delete a Fashion_and_beauty entity by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted Fashion_and_beauty"),
        @ApiResponse(responseCode = "404", description = "Fashion_and_beauty not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteFashion_and_beauty(@Parameter(description = "ID of the Fashion_and_beauty to delete") @PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
