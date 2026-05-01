package com.cleancity.backend.repository;

import com.cleancity.backend.entity.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {

    Optional<Volunteer> findByEmail(String email);

    List<Volunteer> findTop10ByOrderByCompletedCountDesc();
}