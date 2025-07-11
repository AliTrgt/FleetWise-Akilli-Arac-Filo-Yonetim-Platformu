package com.example.MaintenanceService.dto.maintenance.response;

import com.example.MaintenanceService.entity.MaintenanceStatus;
import com.example.MaintenanceService.entity.MaintenanceType;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class MaintenanceViewModel {
    private UUID id;
    private UUID vehicleId;
    private MaintenanceType maintenanceType;
    private LocalDate maintenanceDate;
    private LocalDate estimatedCompletionDate;
    private Double cost;
    private String description;
    private MaintenanceStatus status;
    private String image;
    private Boolean deleted;
    private Boolean confirmed;
}
