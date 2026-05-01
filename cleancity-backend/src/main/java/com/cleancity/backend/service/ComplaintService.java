package com.cleancity.backend.service;

import com.cleancity.backend.entity.Complaint;
import com.cleancity.backend.entity.User;
import com.cleancity.backend.repository.ComplaintRepository;
import com.cleancity.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@Service
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final UserRepository userRepository;

    public ComplaintService(ComplaintRepository complaintRepository,
                            UserRepository userRepository) {
        this.complaintRepository = complaintRepository;
        this.userRepository = userRepository;
    }

    public Complaint createComplaint(
            String description,
            String location,
            String status,
            MultipartFile image,
            String email
    ) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Complaint complaint = new Complaint();
        complaint.setDescription(description);
        complaint.setLocation(location);
        complaint.setStatus(status);
        complaint.setUser(user);

        if (image != null && !image.isEmpty()) {
            try {
                if (image.getSize() > 5 * 1024 * 1024) {
                    throw new RuntimeException("Image size must be less than 5MB");
                }

                String contentType = image.getContentType();

                if (contentType == null ||
                        !(
                                contentType.equals("image/jpeg") ||
                                        contentType.equals("image/png") ||
                                        contentType.equals("image/jpg")
                        )) {
                    throw new RuntimeException("Only JPG, JPEG, and PNG images are allowed");
                }

                String uploadDir = System.getProperty("user.dir") + File.separator + "uploads";
                File folder = new File(uploadDir);

                if (!folder.exists()) {
                    folder.mkdirs();
                }

                String originalFileName = image.getOriginalFilename();
                String fileName = System.currentTimeMillis() + "_" + originalFileName;

                File savedFile = new File(folder, fileName);
                image.transferTo(savedFile);

                complaint.setImageUrl(fileName);

            } catch (Exception e) {
                throw new RuntimeException("Image upload failed: " + e.getMessage());
            }
        }

        return complaintRepository.save(complaint);
    }

    public List<Complaint> getUserComplaints(String email) {
        return complaintRepository.findByUserEmail(email);
    }

    public Complaint getComplaintById(Long id) {
        return complaintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));
    }

    public Complaint updateComplaint(Long id, Complaint updated, String email) {

        Complaint existing = getComplaintById(id);

        boolean isOwner = existing.getUser().getEmail().equals(email);

        boolean isAdmin = userRepository.findByEmail(email)
                .map(user -> user.getRole().equals("ADMIN"))
                .orElse(false);

        if (!isOwner && !isAdmin) {
            throw new RuntimeException("You are not allowed to update this complaint");
        }

        if (updated.getDescription() != null) {
            existing.setDescription(updated.getDescription());
        }

        if (updated.getLocation() != null) {
            existing.setLocation(updated.getLocation());
        }

        if (updated.getStatus() != null) {
            existing.setStatus(updated.getStatus());
        }

        return complaintRepository.save(existing);
    }

    public void deleteComplaint(Long id, String email) {

        Complaint existing = getComplaintById(id);

        boolean isOwner = existing.getUser().getEmail().equals(email);

        boolean isAdmin = userRepository.findByEmail(email)
                .map(user -> user.getRole().equals("ADMIN"))
                .orElse(false);

        if (!isOwner && !isAdmin) {
            throw new RuntimeException("You are not allowed to delete this complaint");
        }

        complaintRepository.deleteById(id);
    }
}