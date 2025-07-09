package com.example.TripService.dto.trip.request;

import com.example.TripService.entity.TripStatus;
import com.example.TripService.entity.TripType;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class InsertTrip {
    private UUID driverId;
    private UUID vehicleId;
    private LocalDate startTime;
    private LocalDate endTime;
    private String startLocation;
    private String endLocation;
    private TripType tripType;

}
