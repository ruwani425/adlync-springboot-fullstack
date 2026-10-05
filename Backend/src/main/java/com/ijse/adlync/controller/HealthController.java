package com.ijse.adlync.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Tag(name = "Health", description = "Lightweight health check and keep-alive endpoints")
public class HealthController {

    @GetMapping("/health")
    @Operation(summary = "Health Check", description = "Returns UP status with minimal payload for cron-job and monitoring")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP"));
    }
}
