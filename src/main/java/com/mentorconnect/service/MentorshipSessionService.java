package com.mentorconnect.service;

import com.mentorconnect.dto.SessionRequestDTO;
import com.mentorconnect.dto.SessionResponseDTO;
import com.mentorconnect.entity.MentorshipPair;
import com.mentorconnect.entity.MentorshipSession;
import com.mentorconnect.entity.SessionStatus;
import com.mentorconnect.exception.ResourceNotFoundException;
import com.mentorconnect.repository.MentorshipSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MentorshipSessionService {

    private final MentorshipSessionRepository sessionRepository;
    private final MentorshipPairService pairService;

    public MentorshipSessionService(MentorshipSessionRepository sessionRepository,
                                    MentorshipPairService pairService) {
        this.sessionRepository = sessionRepository;
        this.pairService = pairService;
    }

    public SessionResponseDTO scheduleSession(SessionRequestDTO dto) {
        MentorshipPair pair = pairService.getPairEntity(dto.getPairId());

        MentorshipSession session = new MentorshipSession();
        session.setPair(pair);
        session.setTopic(dto.getTopic());
        session.setScheduledTime(dto.getScheduledTime());
        session.setDurationMinutes(dto.getDurationMinutes() != null ? dto.getDurationMinutes() : 45);
        session.setStatus(SessionStatus.SCHEDULED);
        session.setMeetingLink(dto.getMeetingLink() != null && !dto.getMeetingLink().isEmpty()
                ? dto.getMeetingLink()
                : "https://meet.google.com/" + generateMeetCode());
        session.setNotes(dto.getNotes());

        MentorshipSession saved = sessionRepository.save(session);
        return mapToDTO(saved);
    }

    public SessionResponseDTO updateSessionStatus(Long sessionId, SessionStatus status) {
        MentorshipSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found with id: " + sessionId));

        session.setStatus(status);
        MentorshipSession updated = sessionRepository.save(session);
        return mapToDTO(updated);
    }

    @Transactional(readOnly = true)
    public List<SessionResponseDTO> getAllSessions() {
        return sessionRepository.findAllByOrderByScheduledTimeDesc().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SessionResponseDTO> getSessionsForPair(Long pairId) {
        return sessionRepository.findByPairIdOrderByScheduledTimeDesc(pairId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public SessionResponseDTO mapToDTO(MentorshipSession session) {
        SessionResponseDTO dto = new SessionResponseDTO();
        dto.setId(session.getId());
        dto.setPairId(session.getPair().getId());
        dto.setAlumniName(session.getPair().getAlumni().getName());
        dto.setStudentName(session.getPair().getStudent().getName());
        dto.setTopic(session.getTopic());
        dto.setScheduledTime(session.getScheduledTime());
        dto.setDurationMinutes(session.getDurationMinutes());
        dto.setStatus(session.getStatus());
        dto.setMeetingLink(session.getMeetingLink());
        dto.setNotes(session.getNotes());
        dto.setCreatedAt(session.getCreatedAt());
        return dto;
    }

    private String generateMeetCode() {
        return "mc-" + (int)(Math.random() * 900 + 100) + "-" + (int)(Math.random() * 900 + 100);
    }
}
