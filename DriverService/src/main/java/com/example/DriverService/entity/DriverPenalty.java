package com.example.DriverService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class DriverPenalty {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "driver_id",nullable = false)
    private Driver driver;

    @Enumerated(EnumType.STRING)
    @Column(name = "penalty_type",nullable = false)
    private PenaltyType penaltyType;

    @Column(name = "penalty_score")
    private Double penaltyScore;

    private String description;

    @Column(name = "even_time",nullable = false)
    private LocalDate eventTime;

    @Column(nullable = false)
    private Boolean deleted = false;
}
