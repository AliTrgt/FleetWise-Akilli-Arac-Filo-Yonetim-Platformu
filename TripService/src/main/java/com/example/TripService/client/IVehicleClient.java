package com.example.TripService.client;

import com.example.TripService.dto.client.VehicleViewModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "vehicle-service",path = "/api/vehicle")
public interface IVehicleClient {

    @GetMapping("/assigned/{id}")
    VehicleViewModel findVehicleByIdAndAssignedTrue(@PathVariable UUID id);

}
