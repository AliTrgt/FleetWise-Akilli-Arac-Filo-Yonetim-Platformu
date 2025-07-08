package com.example.VehicleService.dto.vehicle.request;

import com.example.VehicleService.entity.VehicleStatus;
import com.example.VehicleService.entity.VehicleType;
import lombok.Data;

import java.util.UUID;

@Data
public class InsertVehicle {
    private String plateNumber;
    private String color;
    private String model;
    private String brand;
    private Double mileage;
    private VehicleStatus status;
    private VehicleType vehicleType;
}
