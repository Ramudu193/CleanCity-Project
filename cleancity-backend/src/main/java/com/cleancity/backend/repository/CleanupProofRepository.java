package com.cleancity.backend.repository;

import com.cleancity.backend.entity.CleanupProof;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CleanupProofRepository extends JpaRepository<CleanupProof, Long> {

    long countByStatus(String status);
}