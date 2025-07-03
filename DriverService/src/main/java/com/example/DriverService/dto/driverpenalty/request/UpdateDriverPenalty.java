package com.example.DriverService.dto.driverpenalty.request;

import lombok.Data;

@Data
public class UpdateDriverPenalty {
    private String penaltyType;
    private String description;
}
