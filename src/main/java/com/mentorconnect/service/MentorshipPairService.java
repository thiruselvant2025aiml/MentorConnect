package com.mentorconnect.service;

import com.mentorconnect.dto.PairRequestDTO;
import com.mentorconnect.dto.PairResponseDTO;
import com.mentorconnect.entity.Alumni;
import com.mentorconnect.entity.MentorshipPair;
import com.mentorconnect.entity.PairStatus;
import com.mentorconnect.entity.Student;
import com.mentorconnect.exception.DuplicatePairException;
import com.mentorconnect.exception.MentorCapacityExceededException;
import com.mentorconnect.exception.ResourceNotFoundException;
import com.mentorconnect.repository.MentorshipPairRepository;
import com.mentorconnect.repository.MentorshipSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MentorshipPairService {

    private final MentorshipPairRepository pairRepository;
    private final MentorshipSessionRepository sessionRepository;
    private final AlumniService alumniService;
    private final StudentService studentService;

    public MentorshipPairService(MentorshipPairRepository pairRepository,
                                 MentorshipSessionRepository sessionRepository,
                                 AlumniService alumniService,
                                 StudentService studentService) {
        this.pairRepository = pairRepository;
        this.sessionRepository = sessionRepository;
        this.alumniService = alumniService;
        this.studentService = studentService;
    }

    /**
     * Creates a new mentorship match.
     * STRICT BUSINESS RULE ENFORCEMENT:
     * - A mentor cannot be matched beyond their declared maximum concurrent mentee limit.
     * - Checked in service layer before saving.
     */
    public PairResponseDTO createPair(PairRequestDTO dto) {
        Alumni alumni = alumniService.getAlumniEntity(dto.getAlumniId());
        Student student = studentService.getStudentEntity(dto.getStudentId());

        // Check if an active pair already exists between this mentor and student
        boolean alreadyActive = pairRepository.existsByAlumniIdAndStudentIdAndStatus(
                alumni.getId(), student.getId(), PairStatus.ACTIVE);
        if (alreadyActive) {
            throw new DuplicatePairException("An active mentorship already exists between student '"
                    + student.getName() + "' and mentor '" + alumni.getName() + "'.");
        }

        // Check concurrent active mentees against declared max limit
        long activeCount = pairRepository.countByAlumniIdAndStatus(alumni.getId(), PairStatus.ACTIVE);
        if (activeCount >= alumni.getMaxMentees()) {
            throw new MentorCapacityExceededException("Mentor '" + alumni.getName() 
                    + "' has reached their declared maximum concurrent mentee limit (" 
                    + alumni.getMaxMentees() + "). Cannot accept new mentees at this time.");
        }

        MentorshipPair pair = new MentorshipPair(alumni, student);
        MentorshipPair saved = pairRepository.save(pair);
        return mapToDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<PairResponseDTO> getAllPairs(PairStatus status) {
        List<MentorshipPair> pairs = status != null 
                ? pairRepository.findByStatus(status) 
                : pairRepository.findAll();

        return pairs.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PairResponseDTO getPairById(Long id) {
        MentorshipPair pair = pairRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mentorship pair not found with id: " + id));
        return mapToDTO(pair);
    }

    public MentorshipPair getPairEntity(Long id) {
        return pairRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mentorship pair not found with id: " + id));
    }

    public PairResponseDTO updatePairStatus(Long id, PairStatus newStatus) {
        MentorshipPair pair = getPairEntity(id);
        pair.setStatus(newStatus);
        if (newStatus == PairStatus.COMPLETED || newStatus == PairStatus.CANCELLED) {
            pair.setCompletedAt(LocalDateTime.now());
        }
        MentorshipPair updated = pairRepository.save(pair);
        return mapToDTO(updated);
    }

    public PairResponseDTO mapToDTO(MentorshipPair pair) {
        PairResponseDTO dto = new PairResponseDTO();
        dto.setId(pair.getId());
        dto.setAlumniId(pair.getAlumni().getId());
        dto.setAlumniName(pair.getAlumni().getName());
        dto.setAlumniCompany(pair.getAlumni().getCompany());
        dto.setAlumniAvatar(pair.getAlumni().getAvatarUrl());

        dto.setStudentId(pair.getStudent().getId());
        dto.setStudentName(pair.getStudent().getName());
        dto.setStudentDepartment(pair.getStudent().getDepartment());
        dto.setStudentAvatar(pair.getStudent().getAvatarUrl());

        dto.setStatus(pair.getStatus());
        dto.setPairedAt(pair.getPairedAt());
        dto.setCompletedAt(pair.getCompletedAt());

        long sessionCount = sessionRepository.countByPairId(pair.getId());
        dto.setTotalSessions(sessionCount);

        return dto;
    }
}
