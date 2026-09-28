package com.mentorconnect.controller;

import com.mentorconnect.dto.ApiResponse;
import com.mentorconnect.dto.PairRequestDTO;
import com.mentorconnect.dto.PairResponseDTO;
import com.mentorconnect.entity.PairStatus;
import com.mentorconnect.service.MentorshipPairService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mentorship-pairs")
@Tag(name = "Mentorship Pairings", description = "Endpoints for pairing mentors with students and enforcing capacity limits")
public class MentorshipPairController {

    private final MentorshipPairService pairService;

    public MentorshipPairController(MentorshipPairService pairService) {
        this.pairService = pairService;
    }

    /**
     * Create mentorship pair.
     * Enforces Business Rule: Mentor cannot be matched beyond their declared maximum concurrent mentee limit.
     */
    @PostMapping
    @Operation(summary = "Match student with alumni mentor (Enforces max concurrent mentee limit)")
    public ResponseEntity<ApiResponse<PairResponseDTO>> createPair(@Valid @RequestBody PairRequestDTO dto) {
        PairResponseDTO created = pairService.createPair(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Mentorship pair created successfully", created));
    }

    @GetMapping
    @Operation(summary = "List all mentorship pairs (optional filter by status)")
    public ResponseEntity<ApiResponse<List<PairResponseDTO>>> getAllPairs(
            @RequestParam(required = false) PairStatus status) {
        return ResponseEntity.ok(ApiResponse.ok(pairService.getAllPairs(status)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get mentorship pair by ID")
    public ResponseEntity<ApiResponse<PairResponseDTO>> getPairById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(pairService.getPairById(id)));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update pair status (ACTIVE, COMPLETED, CANCELLED)")
    public ResponseEntity<ApiResponse<PairResponseDTO>> updateStatus(
            @PathVariable Long id,
            @RequestParam PairStatus status) {
        return ResponseEntity.ok(ApiResponse.ok("Status updated", pairService.updatePairStatus(id, status)));
    }
}
