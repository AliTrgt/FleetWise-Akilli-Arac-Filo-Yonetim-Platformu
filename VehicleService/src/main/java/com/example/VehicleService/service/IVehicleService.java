package com.example.VehicleService.service;

import com.example.VehicleService.dto.vehicle.request.InsertVehicle;
import com.example.VehicleService.dto.vehicle.request.UpdateVehicle;
import com.example.VehicleService.dto.vehicle.response.VehicleViewModel;
import com.example.VehicleService.entity.VehicleStatus;

import java.util.List;
import java.util.UUID;

public interface IVehicleService {

    List<VehicleViewModel> getAll();

    void assignDriverToVehicle(UUID driverId,UUID vehicleId) throws Exception;

    void unAssignDriverToVehicle(UUID vehicleId) throws Exception;

    void insert(String authHeader,InsertVehicle vehicle) throws Exception;

    void update(UUID vehicleId, UpdateVehicle vehicle) throws Exception;

    void delete(UUID id) throws Exception;

    List<VehicleViewModel> findByDriverId(UUID driverId);

    VehicleViewModel findById(UUID vehicleId) throws Exception;

    VehicleViewModel findByBrand(String brand);

    VehicleViewModel findByStatus(VehicleStatus status);

    VehicleViewModel findByPlateNumber(String plateNumber);

    VehicleViewModel findVehicleByIdAndAssignedTrue(UUID vehicleId);



}
