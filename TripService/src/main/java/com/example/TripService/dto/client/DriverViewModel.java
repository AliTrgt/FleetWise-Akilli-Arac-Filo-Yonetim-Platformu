package com.example.TripService.dto.client;


import lombok.Data;

import java.sql.Date;
import java.util.UUID;

@Data
public class DriverViewModel {
    private UUID id;
    private int personId;
    private String licenseNumber;
    private Date licenseExpiryDate;
    private String status;
    private Double drivingScore;
    private Double salary;
    private String address;
    private Boolean deleted;
}

