package com.mentorconnect.service;

import com.mentorconnect.dto.MentorMatchDTO;
import com.mentorconnect.entity.Alumni;
import com.mentorconnect.entity.InterestTag;
import com.mentorconnect.entity.PairStatus;
import com.mentorconnect.entity.Student;
import com.mentorconnect.repository.AlumniRepository;
import com.mentorconnect.repository.MentorshipPairRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MatchingService {

    private final StudentService studentService;
    private final AlumniRepository alumniRepository;
    private final MentorshipPairRepository pairRepository;

    public MatchingService(StudentService studentService,
                           AlumniRepository alumniRepository,
                           MentorshipPairRepository pairRepository) {
        this.studentService = studentService;
        this.alumniRepository = alumniRepository;
        this.pairRepository = pairRepository;
    }

    /**
     * Auto-suggest best-matching mentors based on tag overlap for a registered student.
     * Enforces requirement: Match suggestions are ranked by number of overlapping interest tags, highest first.
     */
    public List<MentorMatchDTO> suggestMentorsForStudent(Long studentId) {
        Student student = studentService.getStudentEntity(studentId);
        Set<String> studentTagNames = student.getTargetTags().stream()
                .map(t -> t.getName().trim().toLowerCase())
                .collect(Collectors.toSet());

        return calculateAndRankMatches(studentTagNames);
    }

    /**
     * Auto-suggest best-matching mentors given a custom set of interest tags.
     */
    public List<MentorMatchDTO> suggestMentorsByTags(Set<String> rawTags) {
        Set<String> normalizedTags = rawTags == null ? Collections.emptySet() :
                rawTags.stream()
                        .filter(Objects::nonNull)
                        .map(String::trim)
                        .map(String::toLowerCase)
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.toSet());

        return calculateAndRankMatches(normalizedTags);
    }

    private List<MentorMatchDTO> calculateAndRankMatches(Set<String> targetTags) {
        List<Alumni> allAlumni = alumniRepository.findAll();
        List<MentorMatchDTO> matches = new ArrayList<>();

        for (Alumni alumni : allAlumni) {
            Set<String> alumniTagNames = alumni.getExpertiseTags().stream()
                    .map(InterestTag::getName)
                    .collect(Collectors.toSet());

            Set<String> alumniTagsLower = alumniTagNames.stream()
                    .map(String::toLowerCase)
                    .collect(Collectors.toSet());

            // Compute overlapping tags
            Set<String> sharedTagNames = new HashSet<>();
            for (InterestTag tag : alumni.getExpertiseTags()) {
                if (targetTags.contains(tag.getName().toLowerCase())) {
                    sharedTagNames.add(tag.getName());
                }
            }

            int overlapCount = sharedTagNames.size();
            int matchPercent;
            if (targetTags.isEmpty()) {
                matchPercent = 50; // baseline if student has not selected tags
            } else {
                matchPercent = (int) Math.round(((double) overlapCount / targetTags.size()) * 100.0);
                if (matchPercent > 100) matchPercent = 100;
            }

            // Check active pairs and slots
            long activeCount = pairRepository.countByAlumniIdAndStatus(alumni.getId(), PairStatus.ACTIVE);
            long availableSlots = Math.max(0, alumni.getMaxMentees() - activeCount);

            MentorMatchDTO dto = new MentorMatchDTO();
            dto.setAlumniId(alumni.getId());
            dto.setName(alumni.getName());
            dto.setEmail(alumni.getEmail());
            dto.setCompany(alumni.getCompany());
            dto.setJobTitle(alumni.getJobTitle());
            dto.setAvatarUrl(alumni.getAvatarUrl());
            dto.setBio(alumni.getBio());
            dto.setMaxMentees(alumni.getMaxMentees());
            dto.setActiveMenteesCount(activeCount);
            dto.setAvailableSlots(availableSlots);
            dto.setAvailable(availableSlots > 0);
            dto.setOverlapCount(overlapCount);
            dto.setMatchPercentage(matchPercent);
            dto.setSharedTags(sharedTagNames);
            dto.setAllExpertiseTags(alumniTagNames);

            matches.add(dto);
        }

        // Business Rule: Rank suggestions by number of overlapping interest tags, highest first.
        // Tie-breaker: Mentors with open slots first, then higher available slot count.
        matches.sort((a, b) -> {
            int comp = Integer.compare(b.getOverlapCount(), a.getOverlapCount());
            if (comp != 0) return comp;

            int availComp = Boolean.compare(b.isAvailable(), a.isAvailable());
            if (availComp != 0) return availComp;

            return Long.compare(b.getAvailableSlots(), a.getAvailableSlots());
        });

        return matches;
    }
}
