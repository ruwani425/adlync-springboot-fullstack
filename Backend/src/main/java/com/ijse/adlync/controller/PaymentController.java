package com.ijse.adlync.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.ijse.adlync.dto.request.PaymentRequestDTO;
import com.ijse.adlync.dto.response.PaymentResponseDTO;
import com.ijse.adlync.service.PaymentServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/payments")
@Tag(name = "PaymentEntity Management", description = "APIs for managing PaymentEntity entities")
public class PaymentController {

    @Autowired
    private PaymentServiceImpl service;

    @GetMapping
    @Operation(summary = "Get all Payments", description = "Retrieve a list of all Payment entities")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of Payments"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<PaymentResponseDTO>> getAllPayments() {
        List<PaymentResponseDTO> response = service.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Payment by ID", description = "Retrieve a Payment entity by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved Payment"),
        @ApiResponse(responseCode = "404", description = "Payment not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PaymentResponseDTO> getPaymentById(@Parameter(description = "ID of the Payment to retrieve") @PathVariable Long id) {
        PaymentResponseDTO response = service.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create new Payment", description = "Create a new Payment entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created Payment"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PaymentResponseDTO> createPayment(@Parameter(description = "Payment data to create") @RequestBody PaymentRequestDTO requestDTO) {
        PaymentResponseDTO response = service.create(requestDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Payment", description = "Update an existing Payment entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated Payment"),
        @ApiResponse(responseCode = "404", description = "Payment not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PaymentResponseDTO> updatePayment(@Parameter(description = "ID of the Payment to update") @PathVariable Long id, @Parameter(description = "Updated Payment data") @RequestBody PaymentRequestDTO requestDTO) {
        PaymentResponseDTO response = service.update(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Payment", description = "Delete a Payment entity by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted Payment"),
        @ApiResponse(responseCode = "404", description = "Payment not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deletePayment(@Parameter(description = "ID of the Payment to delete") @PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
