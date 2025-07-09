package com.example.TripService.dto.trip.response;

import com.example.TripService.entity.TripStatus;
import com.example.TripService.entity.TripType;
import lombok.Data;
import org.apache.kafka.common.protocol.types.Field;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class TripViewModel {
    private UUID id;
    private UUID driverId;
    private UUID vehicleId;
    private LocalDate startTime;
    private LocalDate endTime;
    private String startLocation;
    private String endLocation;
    private TripStatus status;
    private TripType tripType;
    private Boolean deleted;
}
