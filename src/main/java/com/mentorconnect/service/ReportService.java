package com.mentorconnect.service;

import com.mentorconnect.dto.EngagementReportDTO;
import com.mentorconnect.entity.MentorshipPair;
import com.mentorconnect.entity.MentorshipSession;
import com.mentorconnect.entity.SessionStatus;
import com.mentorconnect.repository.MentorshipPairRepository;
import com.mentorconnect.repository.MentorshipSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class ReportService {

    private final MentorshipPairRepository pairRepository;
    private final MentorshipSessionRepository sessionRepository;

    public ReportService(MentorshipPairRepository pairRepository,
                         MentorshipSessionRepository sessionRepository) {
        this.pairRepository = pairRepository;
        this.sessionRepository = sessionRepository;
    }

    /**
     * Requirement 5: View mentorship engagement report (sessions held per mentor pair).
     */
    public List<EngagementReportDTO> getEngagementReports() {
        List<MentorshipPair> pairs = pairRepository.findAll();
        List<EngagementReportDTO> reports = new ArrayList<>();

        for (MentorshipPair pair : pairs) {
            List<MentorshipSession> sessions = sessionRepository.findByPairIdOrderByScheduledTimeDesc(pair.getId());

            long total = sessions.size();
            long completed = sessions.stream().filter(s -> s.getStatus() == SessionStatus.COMPLETED).count();
            long scheduled = sessions.stream().filter(s -> s.getStatus() == SessionStatus.SCHEDULED).count();
            long cancelled = sessions.stream().filter(s -> s.getStatus() == SessionStatus.CANCELLED).count();

            long totalMinutes = sessions.stream()
                    .filter(s -> s.getStatus() == SessionStatus.COMPLETED)
                    .mapToLong(s -> s.getDurationMinutes() != null ? s.getDurationMinutes() : 0)
                    .sum();

            EngagementReportDTO report = new EngagementReportDTO();
            report.setPairId(pair.getId());
            report.setAlumniId(pair.getAlumni().getId());
            report.setAlumniName(pair.getAlumni().getName());
            report.setAlumniCompany(pair.getAlumni().getCompany());

            report.setStudentId(pair.getStudent().getId());
            report.setStudentName(pair.getStudent().getName());
            report.setStudentDepartment(pair.getStudent().getDepartment());

            report.setPairStatus(pair.getStatus());
            report.setPairedDate(pair.getPairedAt());

            report.setTotalSessions(total);
            report.setCompletedSessions(completed);
            report.setScheduledSessions(scheduled);
            report.setCancelledSessions(cancelled);
            report.setTotalMinutesCompleted(totalMinutes);

            if (!sessions.isEmpty()) {
                report.setLastSessionDate(sessions.get(0).getScheduledTime());
            }

            reports.add(report);
        }

        return reports;
    }

    public Map<String, Object> getOverallSummary() {
        List<EngagementReportDTO> reports = getEngagementReports();

        long totalPairs = reports.size();
        long activePairs = reports.stream().filter(r -> r.getPairStatus().name().equals("ACTIVE")).count();
        long totalSessions = reports.stream().mapToLong(EngagementReportDTO::getTotalSessions).sum();
        long completedSessions = reports.stream().mapToLong(EngagementReportDTO::getCompletedSessions).sum();
        long totalMinutes = reports.stream().mapToLong(EngagementReportDTO::getTotalMinutesCompleted).sum();

        double totalHours = Math.round((totalMinutes / 60.0) * 10.0) / 10.0;
        int completionRate = totalSessions > 0 ? (int) Math.round(((double) completedSessions / totalSessions) * 100.0) : 0;

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalPairs", totalPairs);
        summary.put("activePairs", activePairs);
        summary.put("totalSessions", totalSessions);
        summary.put("completedSessions", completedSessions);
        summary.put("totalHoursMentored", totalHours);
        summary.put("completionRatePercent", completionRate);
        summary.put("pairReports", reports);

        return summary;
    }
}
