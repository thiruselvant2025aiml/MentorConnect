package com.mentorconnect.dto;

import com.mentorconnect.entity.PairStatus;
import java.time.LocalDateTime;

public class PairResponseDTO {
    private Long id;
    private Long alumniId;
    private String alumniName;
    private String alumniCompany;
    private String alumniAvatar;

    private Long studentId;
    private String studentName;
    private String studentDepartment;
    private String studentAvatar;

    private PairStatus status;
    private LocalDateTime pairedAt;
    private LocalDateTime completedAt;
    private long totalSessions;

    public PairResponseDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAlumniId() {
        return alumniId;
    }

    public void setAlumniId(Long alumniId) {
        this.alumniId = alumniId;
    }

    public String getAlumniName() {
        return alumniName;
    }

    public void setAlumniName(String alumniName) {
        this.alumniName = alumniName;
    }

    public String getAlumniCompany() {
        return alumniCompany;
    }

    public void setAlumniCompany(String alumniCompany) {
        this.alumniCompany = alumniCompany;
    }

    public String getAlumniAvatar() {
        return alumniAvatar;
    }

    public void setAlumniAvatar(String alumniAvatar) {
        this.alumniAvatar = alumniAvatar;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentDepartment() {
        return studentDepartment;
    }

    public void setStudentDepartment(String studentDepartment) {
        this.studentDepartment = studentDepartment;
    }

    public String getStudentAvatar() {
        return studentAvatar;
    }

    public void setStudentAvatar(String studentAvatar) {
        this.studentAvatar = studentAvatar;
    }

    public PairStatus getStatus() {
        return status;
    }

    public void setStatus(PairStatus status) {
        this.status = status;
    }

    public LocalDateTime getPairedAt() {
        return pairedAt;
    }

    public void setPairedAt(LocalDateTime pairedAt) {
        this.pairedAt = pairedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public long getTotalSessions() {
        return totalSessions;
    }

    public void setTotalSessions(long totalSessions) {
        this.totalSessions = totalSessions;
    }
}
