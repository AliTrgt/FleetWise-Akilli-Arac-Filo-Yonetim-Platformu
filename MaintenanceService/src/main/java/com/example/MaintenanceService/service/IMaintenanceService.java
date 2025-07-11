package com.example.MaintenanceService.service;

import com.example.MaintenanceService.dto.maintenance.request.InsertMaintenance;
import com.example.MaintenanceService.dto.maintenance.request.UpdateMaintenance;
import com.example.MaintenanceService.dto.maintenance.response.MaintenanceViewModel;
import com.example.MaintenanceService.entity.MaintenanceStatus;

import java.util.List;
import java.util.UUID;

public interface IMaintenanceService {

    List<MaintenanceViewModel> getMaintenanceByVehicleId(UUID vehicleId);

    List<MaintenanceViewModel> getMaintenanceByStatus(MaintenanceStatus status);

    void createMaintenance(InsertMaintenance maintenance) throws Exception;

    void updateMaintenance(UUID id, UpdateMaintenance maintenance) throws Exception;

    void deleteMaintenance(UUID id) throws Exception;

    MaintenanceViewModel getById(UUID id) throws Exception;

    void isVehicleOverDue(UUID maintenanceId) throws Exception;

    void assignMaintenanceToVehicle(UUID maintenanceId,UUID vehicleId) throws Exception;

    void unAssignMaintenanceToVehicle(UUID maintenanceId) throws Exception;

    void markAsCompleted(UUID id) throws Exception;

}
