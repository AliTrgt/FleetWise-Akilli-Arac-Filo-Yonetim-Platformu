package com.example.NotificationService.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
public class VehicleDto {
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
