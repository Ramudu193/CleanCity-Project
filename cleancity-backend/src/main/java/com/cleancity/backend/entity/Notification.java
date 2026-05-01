package com.cleancity.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String title;

    @Column(length = 1000)
    private String message;

    private boolean readStatus = false;

    private LocalDateTime createdAt = LocalDateTime.now();
}