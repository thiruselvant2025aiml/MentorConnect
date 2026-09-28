package com.mentorconnect.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mentorship_pairs", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"alumni_id", "student_id", "status"})
})
public class MentorshipPair {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "alumni_id", nullable = false)
    private Alumni alumni;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PairStatus status = PairStatus.ACTIVE;

    @Column(name = "paired_at", nullable = false)
    private LocalDateTime pairedAt = LocalDateTime.now();

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    public MentorshipPair() {}

    public MentorshipPair(Alumni alumni, Student student) {
        this.alumni = alumni;
        this.student = student;
        this.status = PairStatus.ACTIVE;
        this.pairedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Alumni getAlumni() {
        return alumni;
    }

    public void setAlumni(Alumni alumni) {
        this.alumni = alumni;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
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
}
