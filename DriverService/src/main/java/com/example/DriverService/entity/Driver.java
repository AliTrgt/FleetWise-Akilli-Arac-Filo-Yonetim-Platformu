package com.example.DriverService.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "person_id",nullable = false,unique = true)
    private int personId;

    @OneToMany(mappedBy ="driver",cascade = CascadeType.ALL)
    private List<DriverPenalty> driverPenaltyList = new ArrayList<>();

    @Column(name = "license_number",nullable = false,unique = true)
    private String licenseNumber;

    @Column(name = "license_expiry_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date licenseExpiryDate;

    private Double drivingScore;

    private Double salary;

    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DriverStatus status;

    @Column(nullable = false)
    private Boolean deleted = false;
}
