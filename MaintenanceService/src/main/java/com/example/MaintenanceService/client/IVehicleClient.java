package com.example.MaintenanceService.client;

import com.example.MaintenanceService.dto.client.VehicleViewModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "vehicle-service",path = "/api/vehicle")
public interface IVehicleClient {

    @GetMapping("/single/{vehicleId}")
    VehicleViewModel findById(@PathVariable UUID vehicleId);

}
