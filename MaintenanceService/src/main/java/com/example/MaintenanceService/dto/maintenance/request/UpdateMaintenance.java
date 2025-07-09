package com.example.MaintenanceService.dto.maintenance.request;

import com.example.MaintenanceService.entity.MaintenanceStatus;
import com.example.MaintenanceService.entity.MaintenanceType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateMaintenance {
    private Double cost;
    private MaintenanceStatus status;
}
