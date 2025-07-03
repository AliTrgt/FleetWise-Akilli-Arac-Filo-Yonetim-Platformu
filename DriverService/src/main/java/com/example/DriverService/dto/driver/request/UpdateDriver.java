package com.example.DriverService.dto.driver.request;

import lombok.Data;

import java.time.LocalDate;
import java.sql.Date;

@Data
public class UpdateDriver {
    private String licenseNumber;
    private Date licenseExpiryDate;
    private String status;
    private Double drivingScore;
    private String address;
}
