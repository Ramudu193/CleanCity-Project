package com.cleancity.backend.controller;

import com.cleancity.backend.entity.Complaint;
import com.cleancity.backend.service.ComplaintService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

    private final ComplaintService complaintService;

    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    // CREATE COMPLAINT WITH IMAGE
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Complaint> createComplaint(
            @RequestParam("description") String description,
            @RequestParam("location") String location,
            @RequestParam("status") String status,
            @RequestParam(value = "image", required = false) MultipartFile image,
            Authentication authentication
    ) {
        String email = authentication.getName();

        Complaint savedComplaint = complaintService.createComplaint(
                description,
                location,
                status,
                image,
                email
        );

        return ResponseEntity.ok(savedComplaint);
    }

    // GET USER COMPLAINTS
    @GetMapping
    public ResponseEntity<List<Complaint>> getUserComplaints(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(complaintService.getUserComplaints(email));
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Complaint> getComplaintById(@PathVariable Long id) {
        return ResponseEntity.ok(complaintService.getComplaintById(id));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Complaint> updateComplaint(
            @PathVariable Long id,
            @RequestBody Complaint complaint,
            Authentication authentication) {

        String email = authentication.getName();
        return ResponseEntity.ok(complaintService.updateComplaint(id, complaint, email));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComplaint(
            @PathVariable Long id,
            Authentication authentication) {

        String email = authentication.getName();
        complaintService.deleteComplaint(id, email);
        return ResponseEntity.ok("Deleted successfully");
    }
}