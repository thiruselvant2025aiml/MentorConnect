package com.mentorconnect.dto;

import jakarta.validation.constraints.NotNull;

public class PairRequestDTO {

    @NotNull(message = "Alumni mentor ID is required")
    private Long alumniId;

    @NotNull(message = "Student ID is required")
    private Long studentId;

    public PairRequestDTO() {}

    public PairRequestDTO(Long alumniId, Long studentId) {
        this.alumniId = alumniId;
        this.studentId = studentId;
    }

    public Long getAlumniId() {
        return alumniId;
    }

    public void setAlumniId(Long alumniId) {
        this.alumniId = alumniId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
}
