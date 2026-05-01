package com.cleancity.backend.service;

import com.cleancity.backend.entity.CleanupProof;
import com.cleancity.backend.entity.Complaint;
import com.cleancity.backend.entity.Volunteer;
import com.cleancity.backend.repository.CleanupProofRepository;
import com.cleancity.backend.repository.ComplaintRepository;
import com.cleancity.backend.repository.VolunteerRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CleanupProofService {

    private final CleanupProofRepository proofRepository;
    private final VolunteerRepository volunteerRepository;
    private final ComplaintRepository complaintRepository;
    private final EmailService emailService;
    private final NotificationService notificationService;   // ✅ ADDED

    // ✅ UPDATED CONSTRUCTOR
    public CleanupProofService(CleanupProofRepository proofRepository,
                               VolunteerRepository volunteerRepository,
                               ComplaintRepository complaintRepository,
                               EmailService emailService,
                               NotificationService notificationService) {
        this.proofRepository = proofRepository;
        this.volunteerRepository = volunteerRepository;
        this.complaintRepository = complaintRepository;
        this.emailService = emailService;
        this.notificationService = notificationService; // ✅ ADDED
    }

    // ================= SUBMIT =================
    public CleanupProof submitProof(
            String name,
            String email,
            Long complaintId,
            String message,
            MultipartFile video
    ) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));

        String fileName = null;

        if (video != null && !video.isEmpty()) {
            try {
                if (video.getSize() > 50 * 1024 * 1024) {
                    throw new RuntimeException("Video size must be less than 50MB");
                }

                String contentType = video.getContentType();

                if (contentType == null || !contentType.startsWith("video")) {
                    throw new RuntimeException("Only video files are allowed");
                }

                String originalFileName = video.getOriginalFilename();

                if (originalFileName == null || !originalFileName.toLowerCase().endsWith(".mp4")) {
                    throw new RuntimeException("Only MP4 videos are allowed");
                }

                String uploadDir = System.getProperty("user.dir") + File.separator + "uploads";
                File folder = new File(uploadDir);

                if (!folder.exists()) {
                    folder.mkdirs();
                }

                fileName = System.currentTimeMillis() + "_" + originalFileName;

                File savedFile = new File(folder, fileName);
                video.transferTo(savedFile);

            } catch (Exception e) {
                throw new RuntimeException("Video upload failed: " + e.getMessage());
            }
        }

        CleanupProof proof = new CleanupProof();
        proof.setVolunteerName(name);
        proof.setVolunteerEmail(email);
        proof.setMessage(message);
        proof.setVideoUrl(fileName != null ? "uploads/" + fileName : null);
        proof.setComplaint(complaint);
        proof.setStatus("PENDING");

        return proofRepository.save(proof);
    }

    // ================= GET ALL =================
    public List<CleanupProof> getAllProofs() {
        return proofRepository.findAll();
    }

    // ================= APPROVE =================
    public CleanupProof approveProof(Long id, String remark) {

        CleanupProof proof = proofRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proof not found"));

        if ("APPROVED".equals(proof.getStatus())) {
            throw new RuntimeException("Proof already approved");
        }

        proof.setStatus("APPROVED");
        proof.setAdminRemark(remark);
        proof.setVerifiedAt(LocalDateTime.now());

        // Update complaint
        Complaint complaint = proof.getComplaint();
        complaint.setStatus("RESOLVED");

        // Get/Create volunteer
        Volunteer volunteer = volunteerRepository.findByEmail(proof.getVolunteerEmail())
                .orElseGet(() -> {
                    Volunteer v = new Volunteer();
                    v.setName(proof.getVolunteerName());
                    v.setEmail(proof.getVolunteerEmail());
                    v.setCompletedCount(0);
                    return v;
                });

        // Update count
        volunteer.setCompletedCount(volunteer.getCompletedCount() + 1);

        // Send email
        emailService.sendProofApprovedEmail(
                volunteer.getEmail(),
                volunteer.getName(),
                volunteer.getCompletedCount(),
                remark
        );

        // 🎯 REWARD LOGIC
        if (volunteer.getCompletedCount() == 50 && !volunteer.isRewardEligible()) {
            volunteer.setRewardEligible(true);
            emailService.sendRewardEmail(volunteer.getEmail(), volunteer.getName());
        }

        // ✅ SAVE VOLUNTEER
        volunteerRepository.save(volunteer);

        // ✅ ADD NOTIFICATION (IMPORTANT FIX)
        notificationService.createNotification(
                volunteer.getEmail(),
                "Proof Approved",
                "Your cleanup proof is approved. Completed count: "
                        + volunteer.getCompletedCount()
                        + "/50. Keep going and help build a cleaner city!"
        );

        return proofRepository.save(proof);
    }

    // ================= REJECT =================
    public CleanupProof rejectProof(Long id, String reason) {

        CleanupProof proof = proofRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proof not found"));

        proof.setStatus("REJECTED");
        proof.setRejectionReason(reason);
        proof.setAdminRemark(reason);
        proof.setVerifiedAt(LocalDateTime.now());

        // Send email
        emailService.sendProofRejectedEmail(
                proof.getVolunteerEmail(),
                proof.getVolunteerName(),
                reason
        );

        // ✅ ADD NOTIFICATION (IMPORTANT FIX)
        notificationService.createNotification(
                proof.getVolunteerEmail(),
                "Proof Rejected",
                "Your cleanup proof was rejected. Reason: "
                        + reason
                        + ". Please don't give up. Upload a clearer cleanup video and try again."
        );

        return proofRepository.save(proof);
    }

    // ================= DELETE =================
    public void deleteProof(Long id) {

        CleanupProof proof = proofRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proof not found"));

        if (proof.getVideoUrl() != null) {
            try {
                String videoPath = proof.getVideoUrl();

                if (videoPath.startsWith("uploads/")) {
                    videoPath = videoPath.substring(8);
                }

                File file = new File(System.getProperty("user.dir")
                        + File.separator + "uploads"
                        + File.separator + videoPath);

                if (file.exists()) {
                    file.delete();
                }

            } catch (Exception e) {
                System.out.println("Video delete failed: " + e.getMessage());
            }
        }

        proofRepository.deleteById(id);
    }
}