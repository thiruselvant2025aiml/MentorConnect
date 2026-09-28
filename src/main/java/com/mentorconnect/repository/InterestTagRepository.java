package com.mentorconnect.repository;

import com.mentorconnect.entity.InterestTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface InterestTagRepository extends JpaRepository<InterestTag, Long> {
    Optional<InterestTag> findByNameIgnoreCase(String name);
    Set<InterestTag> findByNameInIgnoreCase(Set<String> names);
}
