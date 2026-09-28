package com.mentorconnect.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

public class AlumniRequestDTO {

    @NotBlank(message = "Mentor name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Company is required")
    private String company;

    @NotBlank(message = "Job title is required")
    private String jobTitle;

    @NotNull(message = "Maximum concurrent mentee limit is required")
    @Min(value = 1, message = "Max mentees limit must be at least 1")
    private Integer maxMentees = 3;

    private String bio;
    private String avatarUrl;
    private Set<String> expertiseTagNames = new HashSet<>();

    public AlumniRequestDTO() {}

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

    public Set<String> getExpertiseTagNames() {
        return expertiseTagNames;
    }

    public void setExpertiseTagNames(Set<String> expertiseTagNames) {
        this.expertiseTagNames = expertiseTagNames;
    }
}
