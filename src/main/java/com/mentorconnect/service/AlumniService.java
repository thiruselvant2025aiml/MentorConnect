package com.mentorconnect.service;

import com.mentorconnect.dto.AlumniRequestDTO;
import com.mentorconnect.dto.AlumniResponseDTO;
import com.mentorconnect.entity.Alumni;
import com.mentorconnect.entity.InterestTag;
import com.mentorconnect.entity.PairStatus;
import com.mentorconnect.exception.ResourceNotFoundException;
import com.mentorconnect.repository.AlumniRepository;
import com.mentorconnect.repository.MentorshipPairRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class AlumniService {

    private final AlumniRepository alumniRepository;
    private final MentorshipPairRepository pairRepository;
    private final InterestTagService tagService;

    public AlumniService(AlumniRepository alumniRepository,
                         MentorshipPairRepository pairRepository,
                         InterestTagService tagService) {
        this.alumniRepository = alumniRepository;
        this.pairRepository = pairRepository;
        this.tagService = tagService;
    }

    public AlumniResponseDTO registerAlumni(AlumniRequestDTO dto) {
        if (alumniRepository.existsByEmailIgnoreCase(dto.getEmail())) {
            throw new IllegalArgumentException("Alumni mentor with email '" + dto.getEmail() + "' already exists.");
        }

        Alumni alumni = new Alumni();
        alumni.setName(dto.getName());
        alumni.setEmail(dto.getEmail());
        alumni.setCompany(dto.getCompany());
        alumni.setJobTitle(dto.getJobTitle());
        alumni.setMaxMentees(dto.getMaxMentees() != null ? dto.getMaxMentees() : 3);
        alumni.setBio(dto.getBio());
        alumni.setAvatarUrl(dto.getAvatarUrl() != null && !dto.getAvatarUrl().isEmpty() 
                ? dto.getAvatarUrl() 
                : "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150");

        Set<InterestTag> tags = tagService.resolveTags(dto.getExpertiseTagNames());
        alumni.setExpertiseTags(tags);

        Alumni saved = alumniRepository.save(alumni);
        return mapToDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<AlumniResponseDTO> getAllAlumni() {
        return alumniRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AlumniResponseDTO getAlumniById(Long id) {
        Alumni alumni = alumniRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alumni not found with id: " + id));
        return mapToDTO(alumni);
    }

    public Alumni getAlumniEntity(Long id) {
        return alumniRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alumni not found with id: " + id));
    }

    public AlumniResponseDTO mapToDTO(Alumni alumni) {
        AlumniResponseDTO dto = new AlumniResponseDTO();
        dto.setId(alumni.getId());
        dto.setName(alumni.getName());
        dto.setEmail(alumni.getEmail());
        dto.setCompany(alumni.getCompany());
        dto.setJobTitle(alumni.getJobTitle());
        dto.setMaxMentees(alumni.getMaxMentees());
        dto.setBio(alumni.getBio());
        dto.setAvatarUrl(alumni.getAvatarUrl());

        long activeCount = pairRepository.countByAlumniIdAndStatus(alumni.getId(), PairStatus.ACTIVE);
        dto.setActiveMenteesCount(activeCount);
        long available = Math.max(0, alumni.getMaxMentees() - activeCount);
        dto.setAvailableSlots(available);
        dto.setAvailable(available > 0);

        dto.setExpertiseTags(alumni.getExpertiseTags().stream()
                .map(InterestTag::getName)
                .collect(Collectors.toSet()));

        return dto;
    }
}
