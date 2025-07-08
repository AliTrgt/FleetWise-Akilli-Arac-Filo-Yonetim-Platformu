package com.example.VehicleService.dto.driverclient;

import lombok.Data;

import java.util.UUID;

@Data
public class AssignDriverToVehicle {
    private UUID driverId;
    private UUID vehicleId;
}
