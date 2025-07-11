package com.example.MaintenanceService.repository;

import com.example.MaintenanceService.entity.Maintenance;
import com.example.MaintenanceService.entity.MaintenanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface IMaintenanceRepository extends JpaRepository<Maintenance, UUID> {

    List<Maintenance> findAllByVehicleIdAndDeletedFalse(UUID vehicleId);

    List<Maintenance> findAllByStatusAndDeletedFalse(MaintenanceStatus status);

    Optional<Maintenance> findByVehicleIdAndDeletedFalse(UUID id);

    Optional<Maintenance> findByIdAndDeletedFalse(UUID id);
}
