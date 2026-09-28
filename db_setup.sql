-- ============================================================
-- MentorConnect Dedicated Database Setup Script
-- Database: mentorconnect_db
-- User: root | Password: root
-- ============================================================

CREATE DATABASE IF NOT EXISTS mentorconnect_db 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE mentorconnect_db;

-- 1. Interest Tags Table
CREATE TABLE IF NOT EXISTS interest_tags (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(60) NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. Alumni Table (with declared maximum concurrent mentee limit)
CREATE TABLE IF NOT EXISTS alumni (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    company VARCHAR(100) NOT NULL,
    job_title VARCHAR(100) NOT NULL,
    max_mentees INT NOT NULL DEFAULT 3,
    bio VARCHAR(500),
    avatar_url VARCHAR(300),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. Students Table
CREATE TABLE IF NOT EXISTS students (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    department VARCHAR(100) NOT NULL,
    graduation_year INT NOT NULL,
    avatar_url VARCHAR(300),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. Alumni Expertise Tags (Many-to-Many Join Table)
CREATE TABLE IF NOT EXISTS alumni_interest_tags (
    alumni_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,
    PRIMARY KEY (alumni_id, tag_id),
    CONSTRAINT fk_alumni_tags_alumni FOREIGN KEY (alumni_id) REFERENCES alumni(id) ON DELETE CASCADE,
    CONSTRAINT fk_alumni_tags_tag FOREIGN KEY (tag_id) REFERENCES interest_tags(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. Student Target Tags (Many-to-Many Join Table)
CREATE TABLE IF NOT EXISTS student_interest_tags (
    student_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,
    PRIMARY KEY (student_id, tag_id),
    CONSTRAINT fk_student_tags_student FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    CONSTRAINT fk_student_tags_tag FOREIGN KEY (tag_id) REFERENCES interest_tags(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 6. Mentorship Pairs Table (Enforces active pairing)
CREATE TABLE IF NOT EXISTS mentorship_pairs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    alumni_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    status ENUM('ACTIVE', 'COMPLETED', 'CANCELLED') NOT NULL DEFAULT 'ACTIVE',
    paired_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_at DATETIME NULL,
    CONSTRAINT fk_pair_alumni FOREIGN KEY (alumni_id) REFERENCES alumni(id) ON DELETE RESTRICT,
    CONSTRAINT fk_pair_student FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 7. Mentorship Sessions Table (Session tracking and completion)
CREATE TABLE IF NOT EXISTS mentorship_sessions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pair_id BIGINT NOT NULL,
    topic VARCHAR(150) NOT NULL,
    scheduled_time DATETIME NOT NULL,
    duration_minutes INT NOT NULL DEFAULT 45,
    status ENUM('SCHEDULED', 'COMPLETED', 'CANCELLED') NOT NULL DEFAULT 'SCHEDULED',
    meeting_link VARCHAR(300),
    notes TEXT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_session_pair FOREIGN KEY (pair_id) REFERENCES mentorship_pairs(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- SEED INITIAL MOCK DATA
-- ============================================================

-- Tags
INSERT IGNORE INTO interest_tags (id, name) VALUES 
(1, 'Java'),
(2, 'Spring Boot'),
(3, 'Microservices'),
(4, 'System Design'),
(5, 'Cloud Architecture'),
(6, 'Machine Learning'),
(7, 'Python'),
(8, 'DevOps & Docker'),
(9, 'React'),
(10, 'Career & Resume Prep');

-- Alumni
INSERT IGNORE INTO alumni (id, name, email, company, job_title, max_mentees, bio, avatar_url) VALUES 
(1, 'Sarah Jenkins', 'sarah.j@google.com', 'Google', 'Staff Software Engineer', 3, 'Backend engineering, distributed systems, and career mentoring.', 'https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=150'),
(2, 'David Rivera', 'david.r@stripe.com', 'Stripe', 'Head of Engineering', 2, 'Financial infrastructure, fintech scaling, and microservices.', 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150'),
(3, 'Michael Chen', 'michael.c@amazon.com', 'Amazon AWS', 'Principal Cloud Architect', 4, 'AWS cloud infrastructure, distributed databases, high availability.', 'https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=150'),
(4, 'Aisha Khan', 'aisha.k@meta.com', 'Meta', 'Senior ML Research Scientist', 2, 'Applied Deep Learning, NLP, and technical interview preparation.', 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150');

-- Alumni Tags
INSERT IGNORE INTO alumni_interest_tags (alumni_id, tag_id) VALUES 
(1, 1), (1, 2), (1, 3), (1, 4), -- Sarah: Java, Spring Boot, Microservices, System Design
(2, 1), (2, 2), (2, 3), (2, 8), -- David: Java, Spring Boot, Microservices, DevOps
(3, 4), (3, 5), (3, 8), (3, 10),-- Michael: System Design, Cloud, DevOps, Career
(4, 6), (4, 7), (4, 10);        -- Aisha: ML, Python, Career Prep

-- Students
INSERT IGNORE INTO students (id, name, email, department, graduation_year, avatar_url) VALUES 
(1, 'Alex Rivera', 'alex.rivera@university.edu', 'Computer Science', 2025, 'https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=150'),
(2, 'Emily Watson', 'emily.w@university.edu', 'Information Technology', 2026, 'https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150'),
(3, 'Rahul Sharma', 'rahul.s@university.edu', 'Data Science', 2025, 'https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=150');

-- Student Tags
INSERT IGNORE INTO student_interest_tags (student_id, tag_id) VALUES 
(1, 1), (1, 2), (1, 3), (1, 4), -- Alex: Java, Spring Boot, Microservices, System Design (Matches Sarah 100%, David 75%)
(2, 4), (2, 5), (2, 10),        -- Emily: System Design, Cloud, Career Prep
(3, 6), (3, 7);                 -- Rahul: Machine Learning, Python

-- Mentorship Pairs
INSERT IGNORE INTO mentorship_pairs (id, alumni_id, student_id, status, paired_at) VALUES 
(1, 1, 1, 'ACTIVE', NOW() - INTERVAL 10 DAY),
(2, 3, 2, 'ACTIVE', NOW() - INTERVAL 5 DAY);

-- Mentorship Sessions
INSERT IGNORE INTO mentorship_sessions (id, pair_id, topic, scheduled_time, duration_minutes, status, meeting_link, notes) VALUES 
(1, 1, 'Spring Boot & Microservices Architecture Deep Dive', NOW() - INTERVAL 3 DAY, 60, 'COMPLETED', 'https://meet.google.com/abc-defg-hij', 'Reviewed event-driven messaging with Kafka and JPA best practices.'),
(2, 1, 'Mock System Design Interview: Scalable URL Shortener', NOW() + INTERVAL 2 DAY, 45, 'SCHEDULED', 'https://meet.google.com/xyz-uvwx-rst', 'Prepare trade-offs between SQL and NoSQL for indexing.'),
(3, 2, 'AWS Cloud Foundations & IAM Security Review', NOW() - INTERVAL 1 DAY, 45, 'COMPLETED', 'https://meet.google.com/qwe-rtyu-iop', 'Configured VPC, subnets, and RDS database instance.');
