package com.ijse.adlync.controller;

import com.ijse.adlync.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;

import com.ijse.adlync.dto.request.ReportRequestDTO;
import com.ijse.adlync.dto.response.ReportResponseDTO;
import com.ijse.adlync.service.impl.ReportServiceImpl;
import com.ijse.adlync.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
@Tag(name = "Report Management", description = "APIs for managing Report entities")
public class ReportController {

    @Autowired
    private ReportService service;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/status/{status}")
    @Operation(summary = "Get Reports by Status", description = "Retrieve reports filtered by status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved reports"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<ReportResponseDTO>> getReportsByStatus(
            @Parameter(description = "Status to filter by (PENDING, REVIEWED, REJECTED)")
            @PathVariable String status) {
        try {
            List<ReportResponseDTO> response = service.findByStatus(status.toUpperCase());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}/status/{status}")
    @Operation(summary = "Update Report Status", description = "Update report status")
    public ResponseEntity<ReportResponseDTO> updateReportStatus(
            @PathVariable Long id,
            @PathVariable String status) {
        try {
            ReportResponseDTO response = service.updateStatus(id, status.toUpperCase());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

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
    public ResponseEntity<ReportResponseDTO> getReportById(
            @Parameter(description = "ID of the Report to retrieve") @PathVariable Long id) {
        ReportResponseDTO response = service.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create new Report", description = "Create a new Report entity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created Report"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ReportResponseDTO> createReport(
            @Parameter(description = "Report data to create") @RequestBody ReportRequestDTO requestDTO,
            HttpServletRequest request) {

        try {
            String username = null;

            // Try to extract username from token if provided
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                if (jwtUtil.validateToken(token)) {
                    username = jwtUtil.extractUsername(token);
                }
            }

            ReportResponseDTO response = service.create(requestDTO, username);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Report", description = "Update an existing Report entity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated Report"),
            @ApiResponse(responseCode = "404", description = "Report not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ReportResponseDTO> updateReport(
            @Parameter(description = "ID of the Report to update") @PathVariable Long id,
            @Parameter(description = "Updated Report data") @RequestBody ReportRequestDTO requestDTO) {
        try {
            ReportResponseDTO response = service.update(id, requestDTO);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Report", description = "Delete a Report entity by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted Report"),
            @ApiResponse(responseCode = "404", description = "Report not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteReport(
            @Parameter(description = "ID of the Report to delete") @PathVariable Long id) {
        try {
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}