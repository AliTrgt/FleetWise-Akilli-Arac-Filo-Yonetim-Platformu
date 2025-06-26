package com.example.DriverService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "person_id",nullable = false,unique = true)
    private int personId;

    @OneToMany(mappedBy ="driver",cascade = CascadeType.ALL)
    private List<DriverPenalty> driverPenaltyList;

    @Column(name = "license_number",nullable = false,unique = true)
    private String licenseNumber;

    @Column(name = "license_expiry_date")
    private LocalDate licenseExpiryDate;

    private Double drivingScore;

    @Column(name = "phone_number",nullable = false)
    private String phoneNumber;

    private String address;

    @Column(nullable = false)
    private Boolean deleted = false;
}
