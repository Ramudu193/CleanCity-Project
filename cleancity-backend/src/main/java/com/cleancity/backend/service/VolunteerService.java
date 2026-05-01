package com.cleancity.backend.service;

import com.cleancity.backend.entity.Volunteer;
import com.cleancity.backend.repository.VolunteerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VolunteerService {

    private final VolunteerRepository volunteerRepository;

    public VolunteerService(VolunteerRepository volunteerRepository) {
        this.volunteerRepository = volunteerRepository;
    }

    public List<Volunteer> getLeaderboard() {
        return volunteerRepository.findTop10ByOrderByCompletedCountDesc();
    }

    public Volunteer getVolunteerByEmail(String email) {
        return volunteerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Volunteer not found"));
    }
}