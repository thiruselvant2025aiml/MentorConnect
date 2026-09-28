package com.mentorconnect.dto;

import com.mentorconnect.entity.PairStatus;
import java.time.LocalDateTime;

public class EngagementReportDTO {
    private Long pairId;
    private Long alumniId;
    private String alumniName;
    private String alumniCompany;
    private Long studentId;
    private String studentName;
    private String studentDepartment;
    private PairStatus pairStatus;
    private LocalDateTime pairedDate;

    private long totalSessions;
    private long completedSessions;
    private long scheduledSessions;
    private long cancelledSessions;
    private long totalMinutesCompleted;
    private LocalDateTime lastSessionDate;

    public EngagementReportDTO() {}

    public Long getPairId() {
        return pairId;
    }

    public void setPairId(Long pairId) {
        this.pairId = pairId;
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

    public PairStatus getPairStatus() {
        return pairStatus;
    }

    public void setPairStatus(PairStatus pairStatus) {
        this.pairStatus = pairStatus;
    }

    public LocalDateTime getPairedDate() {
        return pairedDate;
    }

    public void setPairedDate(LocalDateTime pairedDate) {
        this.pairedDate = pairedDate;
    }

    public long getTotalSessions() {
        return totalSessions;
    }

    public void setTotalSessions(long totalSessions) {
        this.totalSessions = totalSessions;
    }

    public long getCompletedSessions() {
        return completedSessions;
    }

    public void setCompletedSessions(long completedSessions) {
        this.completedSessions = completedSessions;
    }

    public long getScheduledSessions() {
        return scheduledSessions;
    }

    public void setScheduledSessions(long scheduledSessions) {
        this.scheduledSessions = scheduledSessions;
    }

    public long getCancelledSessions() {
        return cancelledSessions;
    }

    public void setCancelledSessions(long cancelledSessions) {
        this.cancelledSessions = cancelledSessions;
    }

    public long getTotalMinutesCompleted() {
        return totalMinutesCompleted;
    }

    public void setTotalMinutesCompleted(long totalMinutesCompleted) {
        this.totalMinutesCompleted = totalMinutesCompleted;
    }

    public LocalDateTime getLastSessionDate() {
        return lastSessionDate;
    }

    public void setLastSessionDate(LocalDateTime lastSessionDate) {
        this.lastSessionDate = lastSessionDate;
    }
}
