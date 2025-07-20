package com.example.NotificationService.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class MaintenanceDto {
    private UUID id;
    private UUID vehicleId;
    private String maintenanceType;
    private LocalDate maintenanceDate;
    private LocalDate estimatedCompletionDate;
    private Double cost;
    private String description;
    private String status;
    private String image;
    private Boolean deleted;
    private Boolean confirmed;
}
