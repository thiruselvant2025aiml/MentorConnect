package com.mentorconnect.controller;

import com.mentorconnect.dto.ApiResponse;
import com.mentorconnect.dto.StudentRequestDTO;
import com.mentorconnect.dto.StudentResponseDTO;
import com.mentorconnect.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@Tag(name = "Students", description = "Endpoints for student registration and target interest tags")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * Feature 2: Student requests mentorship specifying interest tags.
     */
    @PostMapping
    @Operation(summary = "Register a student with target interest tags")
    public ResponseEntity<ApiResponse<StudentResponseDTO>> registerStudent(@Valid @RequestBody StudentRequestDTO dto) {
        StudentResponseDTO created = studentService.registerStudent(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Student registered successfully", created));
    }

    @GetMapping
    @Operation(summary = "List all registered students")
    public ResponseEntity<ApiResponse<List<StudentResponseDTO>>> getAllStudents() {
        return ResponseEntity.ok(ApiResponse.ok(studentService.getAllStudents()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get student profile by ID")
    public ResponseEntity<ApiResponse<StudentResponseDTO>> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(studentService.getStudentById(id)));
    }
}
