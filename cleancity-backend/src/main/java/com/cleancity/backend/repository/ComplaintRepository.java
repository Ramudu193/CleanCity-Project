package com.cleancity.backend.repository;

import com.cleancity.backend.entity.Complaint;
import com.cleancity.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    List<Complaint> findByUser(User user);

    List<Complaint> findByUserEmail(String email);

    long countByStatus(String status);
}