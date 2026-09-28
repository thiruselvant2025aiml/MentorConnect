package com.mentorconnect.controller;

import com.mentorconnect.dto.ApiResponse;
import com.mentorconnect.dto.SessionRequestDTO;
import com.mentorconnect.dto.SessionResponseDTO;
import com.mentorconnect.entity.SessionStatus;
import com.mentorconnect.service.MentorshipSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
@Tag(name = "Mentoring Sessions", description = "Endpoints for scheduling and tracking mentoring sessions and completion status")
public class MentorshipSessionController {

    private final MentorshipSessionService sessionService;

    public MentorshipSessionController(MentorshipSessionService sessionService) {
        this.sessionService = sessionService;
    }

    /**
     * Feature 4: Track scheduled mentoring sessions.
     */
    @PostMapping
    @Operation(summary = "Schedule a mentoring session")
    public ResponseEntity<ApiResponse<SessionResponseDTO>> scheduleSession(@Valid @RequestBody SessionRequestDTO dto) {
        SessionResponseDTO created = sessionService.scheduleSession(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Session scheduled successfully", created));
    }

    @GetMapping
    @Operation(summary = "List sessions (optionally filter by pairId)")
    public ResponseEntity<ApiResponse<List<SessionResponseDTO>>> getSessions(
            @RequestParam(required = false) Long pairId) {
        List<SessionResponseDTO> sessions = pairId != null
                ? sessionService.getSessionsForPair(pairId)
                : sessionService.getAllSessions();
        return ResponseEntity.ok(ApiResponse.ok(sessions));
    }

    /**
     * Feature 4: Track completion status (SCHEDULED, COMPLETED, CANCELLED).
     */
    @PatchMapping("/{id}/status")
    @Operation(summary = "Update session status (SCHEDULED, COMPLETED, CANCELLED)")
    public ResponseEntity<ApiResponse<SessionResponseDTO>> updateSessionStatus(
            @PathVariable Long id,
            @RequestParam SessionStatus status) {
        SessionResponseDTO updated = sessionService.updateSessionStatus(id, status);
        return ResponseEntity.ok(ApiResponse.ok("Session status updated to " + status, updated));
    }
}
