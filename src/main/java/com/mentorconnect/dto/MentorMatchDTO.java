package com.mentorconnect.dto;

import java.util.HashSet;
import java.util.Set;

public class MentorMatchDTO {
    private Long alumniId;
    private String name;
    private String email;
    private String company;
    private String jobTitle;
    private String avatarUrl;
    private String bio;
    private Integer maxMentees;
    private long activeMenteesCount;
    private long availableSlots;
    private boolean available;
    
    private int overlapCount;
    private int matchPercentage;
    private Set<String> sharedTags = new HashSet<>();
    private Set<String> allExpertiseTags = new HashSet<>();

    public MentorMatchDTO() {}

    public Long getAlumniId() {
        return alumniId;
    }

    public void setAlumniId(Long alumniId) {
        this.alumniId = alumniId;
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

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
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

    public int getOverlapCount() {
        return overlapCount;
    }

    public void setOverlapCount(int overlapCount) {
        this.overlapCount = overlapCount;
    }

    public int getMatchPercentage() {
        return matchPercentage;
    }

    public void setMatchPercentage(int matchPercentage) {
        this.matchPercentage = matchPercentage;
    }

    public Set<String> getSharedTags() {
        return sharedTags;
    }

    public void setSharedTags(Set<String> sharedTags) {
        this.sharedTags = sharedTags;
    }

    public Set<String> getAllExpertiseTags() {
        return allExpertiseTags;
    }

    public void setAllExpertiseTags(Set<String> allExpertiseTags) {
        this.allExpertiseTags = allExpertiseTags;
    }
}
