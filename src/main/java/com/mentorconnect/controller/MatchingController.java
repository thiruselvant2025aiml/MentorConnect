package com.mentorconnect.controller;

import com.mentorconnect.dto.ApiResponse;
import com.mentorconnect.dto.MentorMatchDTO;
import com.mentorconnect.service.MatchingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/matches")
@Tag(name = "Mentor Matching Engine", description = "Endpoints for auto-suggesting mentors ranked by tag overlap")
public class MatchingController {

    private final MatchingService matchingService;

    public MatchingController(MatchingService matchingService) {
        this.matchingService = matchingService;
    }

    /**
     * Feature 3: Auto-suggest best-matching mentors based on tag overlap.
     * Business Rule: Match suggestions are ranked by number of overlapping interest tags, highest first.
     */
    @GetMapping("/suggestions")
    @Operation(summary = "Auto-suggest best-matching mentors for a student (ranked by tag overlap)")
    public ResponseEntity<ApiResponse<List<MentorMatchDTO>>> getSuggestionsForStudent(@RequestParam Long studentId) {
        List<MentorMatchDTO> suggestions = matchingService.suggestMentorsForStudent(studentId);
        return ResponseEntity.ok(ApiResponse.ok("Mentors ranked by tag overlap for student #" + studentId, suggestions));
    }

    @PostMapping("/custom-search")
    @Operation(summary = "Suggest mentors matching custom list of interest tags")
    public ResponseEntity<ApiResponse<List<MentorMatchDTO>>> getSuggestionsByCustomTags(@RequestBody Set<String> tags) {
        List<MentorMatchDTO> suggestions = matchingService.suggestMentorsByTags(tags);
        return ResponseEntity.ok(ApiResponse.ok("Mentors ranked by custom tag overlap", suggestions));
    }
}
