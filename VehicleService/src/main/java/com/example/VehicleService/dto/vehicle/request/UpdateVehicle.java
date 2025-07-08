package com.example.VehicleService.dto.vehicle.request;

import com.example.VehicleService.entity.VehicleStatus;
import com.example.VehicleService.entity.VehicleType;
import lombok.Data;

@Data
public class UpdateVehicle {
    private VehicleStatus status;
    private VehicleType vehicleType;
}
