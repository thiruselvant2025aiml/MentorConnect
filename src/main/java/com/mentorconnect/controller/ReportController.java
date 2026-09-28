package com.mentorconnect.controller;

import com.mentorconnect.dto.ApiResponse;
import com.mentorconnect.dto.EngagementReportDTO;
import com.mentorconnect.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@Tag(name = "Mentorship Reports", description = "Endpoints for viewing engagement reports and sessions held per pair")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * Feature 5: View mentorship engagement report (sessions held per mentor pair).
     */
    @GetMapping("/engagement")
    @Operation(summary = "View mentorship engagement report (sessions held per mentor pair)")
    public ResponseEntity<ApiResponse<List<EngagementReportDTO>>> getEngagementReport() {
        return ResponseEntity.ok(ApiResponse.ok("Mentorship engagement report", reportService.getEngagementReports()));
    }

    @GetMapping("/summary")
    @Operation(summary = "Get high-level mentorship platform summary metrics")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSummary() {
        return ResponseEntity.ok(ApiResponse.ok("Platform summary metrics", reportService.getOverallSummary()));
    }
}
