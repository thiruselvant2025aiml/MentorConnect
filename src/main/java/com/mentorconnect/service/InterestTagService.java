package com.mentorconnect.service;

import com.mentorconnect.entity.InterestTag;
import com.mentorconnect.repository.InterestTagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class InterestTagService {

    private final InterestTagRepository tagRepository;

    public InterestTagService(InterestTagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<InterestTag> getAllTags() {
        return tagRepository.findAll();
    }

    public InterestTag getOrCreateTag(String tagName) {
        String cleanName = tagName.trim();
        return tagRepository.findByNameIgnoreCase(cleanName)
                .orElseGet(() -> tagRepository.save(new InterestTag(cleanName)));
    }

    public Set<InterestTag> resolveTags(Set<String> tagNames) {
        Set<InterestTag> resolved = new HashSet<>();
        if (tagNames != null) {
            for (String name : tagNames) {
                if (name != null && !name.trim().isEmpty()) {
                    resolved.add(getOrCreateTag(name.trim()));
                }
            }
        }
        return resolved;
    }
}
