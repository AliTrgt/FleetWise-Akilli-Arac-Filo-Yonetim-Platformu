package com.example.DriverService.repository;

import com.example.DriverService.entity.DriverVehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DriverVehicleRepository extends JpaRepository<DriverVehicle, UUID> {
}
