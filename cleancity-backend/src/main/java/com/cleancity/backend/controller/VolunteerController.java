package com.cleancity.backend.controller;

import com.cleancity.backend.entity.Volunteer;
import com.cleancity.backend.service.VolunteerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/volunteers")
public class VolunteerController {

    private final VolunteerService volunteerService;

    public VolunteerController(VolunteerService volunteerService) {
        this.volunteerService = volunteerService;
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<List<Volunteer>> getLeaderboard() {
        return ResponseEntity.ok(volunteerService.getLeaderboard());
    }

    @GetMapping("/{email}")
    public ResponseEntity<Volunteer> getVolunteerByEmail(@PathVariable String email) {
        return ResponseEntity.ok(volunteerService.getVolunteerByEmail(email));
    }
}