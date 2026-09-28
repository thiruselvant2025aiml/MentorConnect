package com.mentorconnect.repository;

import com.mentorconnect.entity.MentorshipPair;
import com.mentorconnect.entity.PairStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MentorshipPairRepository extends JpaRepository<MentorshipPair, Long> {
    long countByAlumniIdAndStatus(Long alumniId, PairStatus status);
    boolean existsByAlumniIdAndStudentIdAndStatus(Long alumniId, Long studentId, PairStatus status);
    List<MentorshipPair> findByAlumniId(Long alumniId);
    List<MentorshipPair> findByStudentId(Long studentId);
    List<MentorshipPair> findByStatus(PairStatus status);
}
