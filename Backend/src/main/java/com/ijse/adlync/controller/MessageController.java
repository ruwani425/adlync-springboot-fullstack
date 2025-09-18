package com.ijse.adlync.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.ijse.adlync.dto.request.MessageRequestDTO;
import com.ijse.adlync.dto.response.MessageResponseDTO;
import com.ijse.adlync.service.impl.MessageServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/messages")
@Tag(name = "MessageEntity Management", description = "APIs for managing MessageEntity entities")
public class MessageController {

    @Autowired
    private MessageServiceImpl service;

    @GetMapping
    @Operation(summary = "Get all Messages", description = "Retrieve a list of all Message entities")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of Messages"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<MessageResponseDTO>> getAllMessages() {
        List<MessageResponseDTO> response = service.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Message by ID", description = "Retrieve a Message entity by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved Message"),
        @ApiResponse(responseCode = "404", description = "Message not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<MessageResponseDTO> getMessageById(@Parameter(description = "ID of the Message to retrieve") @PathVariable Long id) {
        MessageResponseDTO response = service.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create new Message", description = "Create a new Message entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created Message"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<MessageResponseDTO> createMessage(@Parameter(description = "Message data to create") @RequestBody MessageRequestDTO requestDTO) {
        MessageResponseDTO response = service.create(requestDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Message", description = "Update an existing Message entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated Message"),
        @ApiResponse(responseCode = "404", description = "Message not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<MessageResponseDTO> updateMessage(@Parameter(description = "ID of the Message to update") @PathVariable Long id, @Parameter(description = "Updated Message data") @RequestBody MessageRequestDTO requestDTO) {
        MessageResponseDTO response = service.update(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Message", description = "Delete a Message entity by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted Message"),
        @ApiResponse(responseCode = "404", description = "Message not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteMessage(@Parameter(description = "ID of the Message to delete") @PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/send")
    @Operation(summary = "Send Message to Seller", description = "Send a message to a seller about an ad")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid message data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<MessageResponseDTO> sendMessageToSeller(@RequestBody MessageRequestDTO requestDTO) {
        // requestDTO should contain: sellerId, adId, subject, message, contactPreferences, senderPhone
        MessageResponseDTO response = service.sendMessage(requestDTO);
        return ResponseEntity.ok(response);
    }
}
