package com.mentorconnect.controller;

import com.mentorconnect.dto.AlumniRequestDTO;
import com.mentorconnect.dto.AlumniResponseDTO;
import com.mentorconnect.dto.ApiResponse;
import com.mentorconnect.service.AlumniService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alumni")
@Tag(name = "Alumni Mentors", description = "Endpoints for alumni registration, capacity, and expertise tags")
public class AlumniController {

    private final AlumniService alumniService;

    public AlumniController(AlumniService alumniService) {
        this.alumniService = alumniService;
    }

    /**
     * Feature 1: Alumni register with expertise tags and available mentoring slots.
     */
    @PostMapping
    @Operation(summary = "Register an alumni mentor with expertise tags and max slots")
    public ResponseEntity<ApiResponse<AlumniResponseDTO>> registerAlumni(@Valid @RequestBody AlumniRequestDTO dto) {
        AlumniResponseDTO created = alumniService.registerAlumni(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Alumni mentor registered successfully", created));
    }

    @GetMapping
    @Operation(summary = "List all alumni mentors with real-time slot availability")
    public ResponseEntity<ApiResponse<List<AlumniResponseDTO>>> getAllAlumni() {
        return ResponseEntity.ok(ApiResponse.ok(alumniService.getAllAlumni()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get alumni mentor by ID")
    public ResponseEntity<ApiResponse<AlumniResponseDTO>> getAlumniById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(alumniService.getAlumniById(id)));
    }
}
