package com.mentorconnect.controller;

import com.mentorconnect.dto.ApiResponse;
import com.mentorconnect.entity.InterestTag;
import com.mentorconnect.service.InterestTagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tags")
@Tag(name = "Interest Tags", description = "Endpoints for managing skills and interest tags")
public class InterestTagController {

    private final InterestTagService tagService;

    public InterestTagController(InterestTagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    @Operation(summary = "List all interest tags")
    public ResponseEntity<ApiResponse<List<InterestTag>>> getAllTags() {
        return ResponseEntity.ok(ApiResponse.ok(tagService.getAllTags()));
    }

    @PostMapping
    @Operation(summary = "Create or retrieve an interest tag")
    public ResponseEntity<ApiResponse<InterestTag>> createTag(@RequestBody Map<String, String> request) {
        String name = request.get("name");
        if (name == null || name.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Tag name is required"));
        }
        InterestTag tag = tagService.getOrCreateTag(name.trim());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Tag ready", tag));
    }
}
