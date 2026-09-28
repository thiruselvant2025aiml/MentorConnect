package com.mentorconnect.config;

import com.mentorconnect.entity.*;
import com.mentorconnect.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class DataInitializer implements CommandLineRunner {

    private final InterestTagRepository tagRepository;
    private final AlumniRepository alumniRepository;
    private final StudentRepository studentRepository;
    private final MentorshipPairRepository pairRepository;
    private final MentorshipSessionRepository sessionRepository;

    public DataInitializer(InterestTagRepository tagRepository,
                           AlumniRepository alumniRepository,
                           StudentRepository studentRepository,
                           MentorshipPairRepository pairRepository,
                           MentorshipSessionRepository sessionRepository) {
        this.tagRepository = tagRepository;
        this.alumniRepository = alumniRepository;
        this.studentRepository = studentRepository;
        this.pairRepository = pairRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void run(String... args) {
        if (tagRepository.count() > 0 && alumniRepository.count() > 0) {
            return; // Data already exists
        }

        // 1. Create Default Tags
        List<String> defaultTags = Arrays.asList(
                "Java", "Spring Boot", "Microservices", "System Design",
                "Cloud Architecture", "Machine Learning", "Python",
                "DevOps & Docker", "React", "Career & Resume Prep"
        );

        Map<String, InterestTag> tagMap = new HashMap<>();
        for (String tagName : defaultTags) {
            tagMap.put(tagName, tagRepository.findByNameIgnoreCase(tagName)
                    .orElseGet(() -> tagRepository.save(new InterestTag(tagName))));
        }

        // 2. Create Default Alumni Mentors
        if (alumniRepository.count() == 0) {
            Alumni sarah = new Alumni();
            sarah.setName("Sarah Jenkins");
            sarah.setEmail("sarah.j@google.com");
            sarah.setCompany("Google");
            sarah.setJobTitle("Staff Software Engineer");
            sarah.setMaxMentees(3);
            sarah.setBio("Backend engineering, distributed systems, and career mentoring.");
            sarah.setAvatarUrl("https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=150");
            sarah.setExpertiseTags(new HashSet<>(Arrays.asList(
                    tagMap.get("Java"), tagMap.get("Spring Boot"),
                    tagMap.get("Microservices"), tagMap.get("System Design")
            )));
            alumniRepository.save(sarah);

            Alumni david = new Alumni();
            david.setName("David Rivera");
            david.setEmail("david.r@stripe.com");
            david.setCompany("Stripe");
            david.setJobTitle("Head of Engineering");
            david.setMaxMentees(2);
            david.setBio("Financial infrastructure, fintech scaling, and microservices.");
            david.setAvatarUrl("https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150");
            david.setExpertiseTags(new HashSet<>(Arrays.asList(
                    tagMap.get("Java"), tagMap.get("Spring Boot"),
                    tagMap.get("Microservices"), tagMap.get("DevOps & Docker")
            )));
            alumniRepository.save(david);

            Alumni michael = new Alumni();
            michael.setName("Michael Chen");
            michael.setEmail("michael.c@amazon.com");
            michael.setCompany("Amazon AWS");
            michael.setJobTitle("Principal Cloud Architect");
            michael.setMaxMentees(4);
            michael.setBio("AWS cloud infrastructure, distributed databases, high availability.");
            michael.setAvatarUrl("https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=150");
            michael.setExpertiseTags(new HashSet<>(Arrays.asList(
                    tagMap.get("System Design"), tagMap.get("Cloud Architecture"),
                    tagMap.get("DevOps & Docker"), tagMap.get("Career & Resume Prep")
            )));
            alumniRepository.save(michael);

            Alumni aisha = new Alumni();
            aisha.setName("Aisha Khan");
            aisha.setEmail("aisha.k@meta.com");
            aisha.setCompany("Meta");
            aisha.setJobTitle("Senior ML Research Scientist");
            aisha.setMaxMentees(2);
            aisha.setBio("Applied Deep Learning, NLP, and technical interview preparation.");
            aisha.setAvatarUrl("https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150");
            aisha.setExpertiseTags(new HashSet<>(Arrays.asList(
                    tagMap.get("Machine Learning"), tagMap.get("Python"),
                    tagMap.get("Career & Resume Prep")
            )));
            alumniRepository.save(aisha);
        }

        // 3. Create Default Students
        if (studentRepository.count() == 0) {
            Student alex = new Student();
            alex.setName("Alex Rivera");
            alex.setEmail("alex.rivera@university.edu");
            alex.setDepartment("Computer Science");
            alex.setGraduationYear(2025);
            alex.setAvatarUrl("https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=150");
            alex.setTargetTags(new HashSet<>(Arrays.asList(
                    tagMap.get("Java"), tagMap.get("Spring Boot"),
                    tagMap.get("Microservices"), tagMap.get("System Design")
            )));
            studentRepository.save(alex);

            Student emily = new Student();
            emily.setName("Emily Watson");
            emily.setEmail("emily.w@university.edu");
            emily.setDepartment("Information Technology");
            emily.setGraduationYear(2026);
            emily.setAvatarUrl("https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150");
            emily.setTargetTags(new HashSet<>(Arrays.asList(
                    tagMap.get("System Design"), tagMap.get("Cloud Architecture"),
                    tagMap.get("Career & Resume Prep")
            )));
            studentRepository.save(emily);

            Student rahul = new Student();
            rahul.setName("Rahul Sharma");
            rahul.setEmail("rahul.s@university.edu");
            rahul.setDepartment("Data Science");
            rahul.setGraduationYear(2025);
            rahul.setAvatarUrl("https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=150");
            rahul.setTargetTags(new HashSet<>(Arrays.asList(
                    tagMap.get("Machine Learning"), tagMap.get("Python")
            )));
            studentRepository.save(rahul);
        }
    }
}
