package com.mentorconnect.repository;

import com.mentorconnect.entity.Alumni;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlumniRepository extends JpaRepository<Alumni, Long> {
    Optional<Alumni> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);
}
