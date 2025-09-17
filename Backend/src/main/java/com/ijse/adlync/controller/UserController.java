package com.ijse.adlync.controller;

import com.ijse.adlync.dto.request.RegisterRequestDTO;
import com.ijse.adlync.dto.response.RegisterResponseDTO;
import com.ijse.adlync.dto.response.UserResponseDTO;
import com.ijse.adlync.service.impl.OtpServiceImpl;
import com.ijse.adlync.service.impl.UserServiceImpl;
import com.ijse.adlync.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@Tag(name = "UserEntity Management", description = "APIs for managing UserEntity entities")
public class UserController {

    @Autowired
    private UserServiceImpl service;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private OtpServiceImpl otpService;

    // Add this to your UserController.java

    @PatchMapping("/change-password")
    @Operation(summary = "Change Password", description = "Change user's password after validating current password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid current password"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Map<String, String>> changePassword(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, String> requestBody) {

        try {
            String token = authHeader.replace("Bearer ", "").trim();
            String username = jwtUtil.extractUsername(token);
            String currentPassword = requestBody.get("currentPassword");
            String newPassword = requestBody.get("newPassword");

            // Validate input
            if (currentPassword == null || currentPassword.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Current password is required"));
            }

            if (newPassword == null || newPassword.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "New password is required"));
            }

            // Change password through service
            service.changePassword(username, currentPassword, newPassword);

            return ResponseEntity.ok(Map.of("message", "Password changed successfully"));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Failed to change password"));
        }
    }

    @PostMapping("/checkEmail")
    @Operation(summary = "Check if Email Exists", description = "Checks if the provided email exists in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email existence status returned"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        boolean exists = service.checkEmailExists(email);
        return ResponseEntity.ok(Map.of("exists", exists));
    }

    @PostMapping("/sendOTP")
    @Operation(summary = "Send OTP for password reset", description = "Checks if email exists and sends OTP if it does")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OTP sent successfully"),
            @ApiResponse(responseCode = "404", description = "Email not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> sendOTP(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        try {
            if (!service.checkEmailExists(email)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found in the system.");
            }
            otpService.generateAndSendOTP(email);
            return ResponseEntity.ok("OTP sent to your email.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/verifyOTP")
    @Operation(summary = "Verify OTP", description = "Verifies the OTP for the given email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OTP verified"),
            @ApiResponse(responseCode = "400", description = "Invalid or expired OTP"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> verifyOTP(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");
        if (otpService.verifyOTP(email, otp)) {
            return ResponseEntity.ok("OTP verified successfully.");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired OTP.");
        }
    }

    @PostMapping("/resetPassword")
    @Operation(summary = "Reset Password", description = "Resets the password for the given email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password reset successfully"),
            @ApiResponse(responseCode = "404", description = "Email not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String newPassword = request.get("newPassword");
        try {
            service.resetPassword(email, newPassword);
            return ResponseEntity.ok("Password reset successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/getUserByToken")
    public ResponseEntity<UserResponseDTO> getUserByToken(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "").trim();
        String username = jwtUtil.extractUsername(token);
        UserResponseDTO user = service.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/update-profile-photo")
    @Operation(summary = "Update Profile Photo", description = "Update user's profile photo URL")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated profile photo"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<UserResponseDTO> updateProfilePhoto(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, String> requestBody) {

        String token = authHeader.replace("Bearer ", "").trim();
        String username = jwtUtil.extractUsername(token);
        String profileImageUrl = requestBody.get("profileImageUrl");

        UserResponseDTO response = service.updateProfilePhoto(username, profileImageUrl);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all Users", description = "Retrieve a list of all User entities")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of Users"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<RegisterResponseDTO>> getAllUsers() {
        List<RegisterResponseDTO> response = service.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    @Operation(summary = "Get all Users", description = "Retrieve a list of all User entities")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of Users"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<UserResponseDTO>> getAllUsersByUsername(String username) {
        List<UserResponseDTO> users = service.findAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get User by ID", description = "Retrieve a User entity by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved User"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<RegisterResponseDTO> getUserById(@Parameter(description = "ID of the User to retrieve") @PathVariable Long id) {
        RegisterResponseDTO response = service.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create new User", description = "Create a new User entity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created User"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<RegisterResponseDTO> createUser(@Parameter(description = "User data to create") @RequestBody RegisterRequestDTO requestDTO) throws Exception {
        RegisterResponseDTO response = service.create(requestDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update User", description = "Update an existing User entity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated User"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<RegisterResponseDTO> updateUser(@Parameter(description = "ID of the User to update") @PathVariable Long id, @Parameter(description = "Updated User data") @RequestBody RegisterRequestDTO requestDTO) {
        RegisterResponseDTO response = service.update(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/set-moderator-password")
    @Operation(summary = "Update Moderator", description = "Update an existing Moderator")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated moderator"),
            @ApiResponse(responseCode = "404", description = "moderator not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<UserResponseDTO> updateModerator(@RequestParam String token, @RequestParam String password) throws Exception {
        System.out.println(token);
        System.out.println(password);
        UserResponseDTO response = service.updateModerator(token, password);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete User", description = "Delete a User entity by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted User"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteUser(@Parameter(description = "ID of the User to delete") @PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
