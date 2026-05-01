package com.cleancity.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "volunteers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Volunteer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;

    private int completedCount = 0;

    private boolean rewardEligible = false;
}