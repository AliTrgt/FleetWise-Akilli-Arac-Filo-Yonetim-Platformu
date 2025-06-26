package com.example.DriverService.dto.driver.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class InsertDriver {
    private int personId;
    private int driverPenaltyId;
    private String licenseNumber;
    private LocalDate licenseExpiryDate;
    private Double drivingScore;
    private String phoneNumber;
    private String address;
}
