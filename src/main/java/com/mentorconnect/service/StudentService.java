package com.mentorconnect.service;

import com.mentorconnect.dto.StudentRequestDTO;
import com.mentorconnect.dto.StudentResponseDTO;
import com.mentorconnect.entity.InterestTag;
import com.mentorconnect.entity.Student;
import com.mentorconnect.exception.ResourceNotFoundException;
import com.mentorconnect.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;
    private final InterestTagService tagService;

    public StudentService(StudentRepository studentRepository, InterestTagService tagService) {
        this.studentRepository = studentRepository;
        this.tagService = tagService;
    }

    public StudentResponseDTO registerStudent(StudentRequestDTO dto) {
        if (studentRepository.existsByEmailIgnoreCase(dto.getEmail())) {
            throw new IllegalArgumentException("Student with email '" + dto.getEmail() + "' already exists.");
        }

        Student student = new Student();
        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        student.setDepartment(dto.getDepartment());
        student.setGraduationYear(dto.getGraduationYear());
        student.setAvatarUrl(dto.getAvatarUrl() != null && !dto.getAvatarUrl().isEmpty()
                ? dto.getAvatarUrl()
                : "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=150");

        Set<InterestTag> tags = tagService.resolveTags(dto.getTargetTagNames());
        student.setTargetTags(tags);

        Student saved = studentRepository.save(student);
        return mapToDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<StudentResponseDTO> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public StudentResponseDTO getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        return mapToDTO(student);
    }

    public Student getStudentEntity(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
    }

    public StudentResponseDTO mapToDTO(Student student) {
        StudentResponseDTO dto = new StudentResponseDTO();
        dto.setId(student.getId());
        dto.setName(student.getName());
        dto.setEmail(student.getEmail());
        dto.setDepartment(student.getDepartment());
        dto.setGraduationYear(student.getGraduationYear());
        dto.setAvatarUrl(student.getAvatarUrl());
        dto.setTargetTags(student.getTargetTags().stream()
                .map(InterestTag::getName)
                .collect(Collectors.toSet()));
        return dto;
    }
}
