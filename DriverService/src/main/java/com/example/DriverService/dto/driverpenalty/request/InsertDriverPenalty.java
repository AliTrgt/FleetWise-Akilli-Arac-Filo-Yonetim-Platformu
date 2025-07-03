package com.example.DriverService.dto.driverpenalty.request;

import lombok.Data;

import java.util.UUID;

@Data
public class InsertDriverPenalty {
    private UUID driverId;
    private String penaltyType;
    private String description;

}
