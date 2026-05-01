package com.cleancity.backend.controller;

import com.cleancity.backend.entity.CleanupProof;
import com.cleancity.backend.service.CleanupProofService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/proofs")
public class CleanupProofController {

    private final CleanupProofService cleanupProofService;

    public CleanupProofController(CleanupProofService cleanupProofService) {
        this.cleanupProofService = cleanupProofService;
    }

    @PostMapping(value = "/submit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CleanupProof> submitProof(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("complaintId") Long complaintId,
            @RequestParam("message") String message,
            @RequestParam("video") MultipartFile video
    ) {
        CleanupProof proof = cleanupProofService.submitProof(
                name,
                email,
                complaintId,
                message,
                video
        );

        return ResponseEntity.ok(proof);
    }

    @GetMapping
    public ResponseEntity<List<CleanupProof>> getAllProofs() {
        return ResponseEntity.ok(cleanupProofService.getAllProofs());
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<CleanupProof> approveProof(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> body
    ) {
        String remark = "Great work! Your cleanup proof has been approved. Keep solving more complaints.";

        if (body != null && body.containsKey("remark") && body.get("remark") != null) {
            remark = body.get("remark");
        }

        CleanupProof proof = cleanupProofService.approveProof(id, remark);

        return ResponseEntity.ok(proof);
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<CleanupProof> rejectProof(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> body
    ) {
        String reason = "Video proof is not clear. Please upload a clearer cleanup video.";

        if (body != null && body.containsKey("reason") && body.get("reason") != null) {
            reason = body.get("reason");
        }

        CleanupProof proof = cleanupProofService.rejectProof(id, reason);

        return ResponseEntity.ok(proof);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProof(@PathVariable Long id) {
        cleanupProofService.deleteProof(id);
        return ResponseEntity.ok("Proof deleted successfully");
    }
}