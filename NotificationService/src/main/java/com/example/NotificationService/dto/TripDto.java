package com.example.NotificationService.dto;

import lombok.Data;
import org.apache.kafka.common.protocol.types.Field;

import java.time.LocalDate;
import java.util.UUID;
@Data
public class TripDto {

    private UUID id;
    private UUID driverId;
    private UUID vehicleId;
    private LocalDate startTime;
    private LocalDate endTime;
    private String startLocation;
    private String endLocation;
    private String status;
    private String tripType;
    private Boolean deleted;
}
