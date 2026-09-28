package com.mentorconnect.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "alumni")
public class Alumni {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 120)
    private String email;

    @Column(nullable = false, length = 100)
    private String company;

    @Column(name = "job_title", nullable = false, length = 100)
    private String jobTitle;

    @Column(name = "max_mentees", nullable = false)
    private Integer maxMentees = 3;

    @Column(length = 500)
    private String bio;

    @Column(name = "avatar_url", length = 300)
    private String avatarUrl;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "alumni_interest_tags",
        joinColumns = @JoinColumn(name = "alumni_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<InterestTag> expertiseTags = new HashSet<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Alumni() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Integer getMaxMentees() {
        return maxMentees;
    }

    public void setMaxMentees(Integer maxMentees) {
        this.maxMentees = maxMentees;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Set<InterestTag> getExpertiseTags() {
        return expertiseTags;
    }

    public void setExpertiseTags(Set<InterestTag> expertiseTags) {
        this.expertiseTags = expertiseTags;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
