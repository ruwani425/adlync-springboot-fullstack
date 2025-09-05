package com.ijse.adlync.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.ijse.adlync.dto.request.EducationRequestDTO;
import com.ijse.adlync.dto.response.EducationResponseDTO;
import com.ijse.adlync.service.impl.EducationServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/educations")
@Tag(name = "EducationEntity Management", description = "APIs for managing EducationEntity entities")
public class EducationController {

    @Autowired
    private EducationServiceImpl service;

    @GetMapping
    @Operation(summary = "Get all Educations", description = "Retrieve a list of all Education entities")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of Educations"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<EducationResponseDTO>> getAllEducations() {
        List<EducationResponseDTO> response = service.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Education by ID", description = "Retrieve a Education entity by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved Education"),
        @ApiResponse(responseCode = "404", description = "Education not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<EducationResponseDTO> getEducationById(@Parameter(description = "ID of the Education to retrieve") @PathVariable Long id) {
        EducationResponseDTO response = service.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create new Education", description = "Create a new Education entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created Education"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<EducationResponseDTO> createEducation(@Parameter(description = "Education data to create") @RequestBody EducationRequestDTO requestDTO) {
        EducationResponseDTO response = service.create(requestDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Education", description = "Update an existing Education entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated Education"),
        @ApiResponse(responseCode = "404", description = "Education not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<EducationResponseDTO> updateEducation(@Parameter(description = "ID of the Education to update") @PathVariable Long id, @Parameter(description = "Updated Education data") @RequestBody EducationRequestDTO requestDTO) {
        EducationResponseDTO response = service.update(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Education", description = "Delete a Education entity by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted Education"),
        @ApiResponse(responseCode = "404", description = "Education not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteEducation(@Parameter(description = "ID of the Education to delete") @PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
