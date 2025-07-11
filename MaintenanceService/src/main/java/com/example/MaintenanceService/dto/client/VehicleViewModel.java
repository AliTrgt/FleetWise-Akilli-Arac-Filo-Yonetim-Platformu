package com.example.MaintenanceService.dto.client;

import lombok.Data;

import java.util.UUID;

@Data
public class VehicleViewModel {
    private UUID id;
    private UUID driverId;
    private String plateNumber;
    private String color;
    private String model;
    private String brand;
    private Double mileage;
    private Boolean deleted;
    private String status;
    private String vehicleType;
    private Boolean assigned;
}
