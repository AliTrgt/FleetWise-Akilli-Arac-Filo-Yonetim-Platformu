package com.example.VehicleService.dto.vehicle.response;

import com.example.VehicleService.entity.VehicleStatus;
import com.example.VehicleService.entity.VehicleType;
import lombok.Data;
import org.apache.kafka.common.protocol.types.Field;

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
    private VehicleStatus status;
    private VehicleType vehicleType;
}
