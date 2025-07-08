package com.example.TripService.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Null;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table
@Data
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID driverId;

    @Column(nullable = false)
    private UUID vehicleId;

    @Column(nullable = false)
    private LocalDate startTime;

    @Column(nullable = false)
    private LocalDate endTime;

    @Column(nullable = false)
    private String startLocation;

    @Column(nullable = false)
    private String endLocation;

    @Enumerated(EnumType.STRING)
    private TripStatus status;

    @Enumerated(EnumType.STRING)
    private TripType tripType;
}
