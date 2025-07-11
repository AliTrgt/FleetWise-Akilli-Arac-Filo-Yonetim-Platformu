package com.example.MaintenanceService.dto.client;

import lombok.Data;

import java.util.UUID;

@Data
public class AssignMaintenanceToVehicle {
    private UUID maintenanceId;
    private UUID vehicleId;
}
