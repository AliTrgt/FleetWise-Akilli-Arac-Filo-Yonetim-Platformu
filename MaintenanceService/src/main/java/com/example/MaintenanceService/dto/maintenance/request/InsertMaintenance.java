package com.example.MaintenanceService.dto.maintenance.request;

import com.example.MaintenanceService.entity.MaintenanceStatus;
import com.example.MaintenanceService.entity.MaintenanceType;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class InsertMaintenance {
    private MaintenanceType maintenanceType;
    private LocalDate maintenanceDate;
    private Double cost;
    private String description;
    private MaintenanceStatus status;
}
