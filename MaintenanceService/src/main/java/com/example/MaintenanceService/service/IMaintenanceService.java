package com.example.MaintenanceService.service;

import com.example.MaintenanceService.dto.maintenance.request.InsertMaintenance;
import com.example.MaintenanceService.dto.maintenance.request.UpdateMaintenance;
import com.example.MaintenanceService.dto.maintenance.response.MaintenanceViewModel;

import java.util.List;
import java.util.UUID;

public interface IMaintenanceService {

    List<MaintenanceViewModel> getMaintenanceByVehicleId(UUID vehicleId);

    void createMaintenance(InsertMaintenance maintenance);

    void updateMaintenance(UUID id, UpdateMaintenance maintenance);

    void deleteMaintenance(UUID id);
}
