package com.example.DriverService.dto.driverpenalty.response;

import com.example.DriverService.entity.PenaltyType;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class DriverPenaltyViewModel {
    private UUID id;
    private UUID driverId;
    private PenaltyType penaltyType;
    private String description;
    private Date eventTime;
}
