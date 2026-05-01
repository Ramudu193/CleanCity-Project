package com.cleancity.backend.controller;

import com.cleancity.backend.repository.CleanupProofRepository;
import com.cleancity.backend.repository.ComplaintRepository;
import com.cleancity.backend.repository.VolunteerRepository;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminStatsController {

    private final ComplaintRepository complaintRepository;
    private final VolunteerRepository volunteerRepository;
    private final CleanupProofRepository cleanupProofRepository;

    public AdminStatsController(
            ComplaintRepository complaintRepository,
            VolunteerRepository volunteerRepository,
            CleanupProofRepository cleanupProofRepository
    ) {
        this.complaintRepository = complaintRepository;
        this.volunteerRepository = volunteerRepository;
        this.cleanupProofRepository = cleanupProofRepository;
    }

    @GetMapping("/stats")
    public Map<String, Long> getStats() {

        Map<String, Long> stats = new HashMap<>();

        stats.put("totalComplaints", complaintRepository.count());
        stats.put("pendingComplaints", complaintRepository.countByStatus("PENDING"));
        stats.put("resolvedComplaints", complaintRepository.countByStatus("RESOLVED"));

        stats.put("totalVolunteers", volunteerRepository.count());

        stats.put("totalProofs", cleanupProofRepository.count());
        stats.put("approvedProofs", cleanupProofRepository.countByStatus("APPROVED"));
        stats.put("rejectedProofs", cleanupProofRepository.countByStatus("REJECTED"));
        stats.put("pendingProofs", cleanupProofRepository.countByStatus("PENDING"));

        return stats;
    }
}