package com.example.DriverService.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.UUID;

@Entity
@Table
@Data
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

    private String description;

    @Column(name = "even_time",nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date eventTime = new Date();

    @Column(nullable = false)
    private Boolean deleted = false;
}
