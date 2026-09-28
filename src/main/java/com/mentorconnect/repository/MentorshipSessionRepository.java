package com.mentorconnect.repository;

import com.mentorconnect.entity.MentorshipSession;
import com.mentorconnect.entity.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MentorshipSessionRepository extends JpaRepository<MentorshipSession, Long> {
    List<MentorshipSession> findByPairIdOrderByScheduledTimeDesc(Long pairId);
    List<MentorshipSession> findByStatusOrderByScheduledTimeAsc(SessionStatus status);
    long countByPairIdAndStatus(Long pairId, SessionStatus status);
    long countByPairId(Long pairId);
    List<MentorshipSession> findAllByOrderByScheduledTimeDesc();
}
