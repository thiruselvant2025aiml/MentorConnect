package com.mentorconnect.dto;

import java.util.HashSet;
import java.util.Set;

public class StudentResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String department;
    private Integer graduationYear;
    private String avatarUrl;
    private Set<String> targetTags = new HashSet<>();

    public StudentResponseDTO() {}

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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(Integer graduationYear) {
        this.graduationYear = graduationYear;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Set<String> getTargetTags() {
        return targetTags;
    }

    public void setTargetTags(Set<String> targetTags) {
        this.targetTags = targetTags;
    }
}
