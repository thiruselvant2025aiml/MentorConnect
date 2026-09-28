package com.mentorconnect.dto;

import java.util.HashSet;
import java.util.Set;

public class AlumniResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String company;
    private String jobTitle;
    private Integer maxMentees;
    private long activeMenteesCount;
    private long availableSlots;
    private boolean available;
    private String bio;
    private String avatarUrl;
    private Set<String> expertiseTags = new HashSet<>();

    public AlumniResponseDTO() {}

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

    public long getActiveMenteesCount() {
        return activeMenteesCount;
    }

    public void setActiveMenteesCount(long activeMenteesCount) {
        this.activeMenteesCount = activeMenteesCount;
    }

    public long getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(long availableSlots) {
        this.availableSlots = availableSlots;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
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

    public Set<String> getExpertiseTags() {
        return expertiseTags;
    }

    public void setExpertiseTags(Set<String> expertiseTags) {
        this.expertiseTags = expertiseTags;
    }
}
