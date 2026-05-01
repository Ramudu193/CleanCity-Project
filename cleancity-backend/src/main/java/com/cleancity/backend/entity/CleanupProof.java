package com.cleancity.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "cleanup_proofs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CleanupProof {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String volunteerName;

    private String volunteerEmail;

    private String message;

    private String videoUrl;

    private String status = "PENDING"; // PENDING, APPROVED, REJECTED

    private String rejectionReason;

    private String adminRemark; // approval/rejection message from admin

    private LocalDateTime submittedAt = LocalDateTime.now();

    private LocalDateTime verifiedAt; // when admin approved/rejected

    @ManyToOne
    @JoinColumn(name = "complaint_id")
    private Complaint complaint;
}