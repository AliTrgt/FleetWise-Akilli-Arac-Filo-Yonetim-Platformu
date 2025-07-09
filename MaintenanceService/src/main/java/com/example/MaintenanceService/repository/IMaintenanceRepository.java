package com.example.MaintenanceService.repository;

import com.example.MaintenanceService.entity.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface IMaintenanceRepository extends JpaRepository<Maintenance, UUID> {

}
