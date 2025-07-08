package com.example.VehicleService.repository;

import com.example.VehicleService.entity.Vehicle;
import com.example.VehicleService.entity.VehicleStatus;
import org.apache.kafka.common.quota.ClientQuotaAlteration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IVehicleRepository extends JpaRepository<Vehicle, UUID> {
    Optional<Vehicle> findByIdAndDeletedFalse(UUID id);
    List<Vehicle> findVehicleByDriverIdAndDeletedFalse(UUID driverId);
    Optional<Vehicle> findByDriverIdAndDeletedFalse(UUID driverId);
    Optional<Vehicle> findVehicleByBrandAndDeletedFalse(String brand);
    Optional<Vehicle> findVehicleByStatusAndDeletedFalse(VehicleStatus status);
    Optional<Vehicle> findByPlateNumberAndDeletedFalse(String plateNumber);
}
