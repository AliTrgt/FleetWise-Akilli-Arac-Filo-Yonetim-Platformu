package com.example.DriverService.dto.driver.request;

import com.example.DriverService.entity.DriverStatus;
import lombok.Data;

import java.sql.Date;
import java.time.LocalDate;

@Data
public class InsertDriver {
    private String licenseNumber;
    private Date licenseExpiryDate;
    private String address;
}
