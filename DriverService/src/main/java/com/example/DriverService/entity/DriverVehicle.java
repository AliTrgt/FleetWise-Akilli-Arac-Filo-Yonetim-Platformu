package com.example.DriverService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DriverVehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "driver_id",nullable = false,unique = true)
    private int driverId;

    @Column(name = "vehicle_id",nullable = false,unique = true)
    private int vehicleId;

    @Column(nullable = false)
    private LocalDate assignedDate;

    @Column(nullable = false)
    private Boolean active = true;

}
