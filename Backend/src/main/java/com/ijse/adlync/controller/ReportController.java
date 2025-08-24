package com.ijse.adlync.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.ijse.adlync.dto.request.ReportRequestDTO;
import com.ijse.adlync.dto.response.ReportResponseDTO;
import com.ijse.adlync.service.ReportServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/reports")
@Tag(name = "ReportEntity Management", description = "APIs for managing ReportEntity entities")
public class ReportController {

    @Autowired
    private ReportServiceImpl service;

    @GetMapping
    @Operation(summary = "Get all Reports", description = "Retrieve a list of all Report entities")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of Reports"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<ReportResponseDTO>> getAllReports() {
        List<ReportResponseDTO> response = service.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Report by ID", description = "Retrieve a Report entity by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved Report"),
        @ApiResponse(responseCode = "404", description = "Report not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ReportResponseDTO> getReportById(@Parameter(description = "ID of the Report to retrieve") @PathVariable Long id) {
        ReportResponseDTO response = service.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create new Report", description = "Create a new Report entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created Report"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ReportResponseDTO> createReport(@Parameter(description = "Report data to create") @RequestBody ReportRequestDTO requestDTO) {
        ReportResponseDTO response = service.create(requestDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Report", description = "Update an existing Report entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated Report"),
        @ApiResponse(responseCode = "404", description = "Report not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ReportResponseDTO> updateReport(@Parameter(description = "ID of the Report to update") @PathVariable Long id, @Parameter(description = "Updated Report data") @RequestBody ReportRequestDTO requestDTO) {
        ReportResponseDTO response = service.update(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Report", description = "Delete a Report entity by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted Report"),
        @ApiResponse(responseCode = "404", description = "Report not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteReport(@Parameter(description = "ID of the Report to delete") @PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
