package com.example.MaintenanceService.controller;

import com.example.MaintenanceService.dto.client.AssignMaintenanceToVehicle;
import com.example.MaintenanceService.dto.maintenance.request.InsertMaintenance;
import com.example.MaintenanceService.dto.maintenance.request.UpdateMaintenance;
import com.example.MaintenanceService.dto.maintenance.response.MaintenanceViewModel;
import com.example.MaintenanceService.entity.MaintenanceStatus;
import com.example.MaintenanceService.service.IMaintenanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/maintenance")
@RequiredArgsConstructor
public class MaintenanceController {

    private final IMaintenanceService maintenanceService;

    @GetMapping("/{vehicleId}")
    public ResponseEntity<List<MaintenanceViewModel>> getAllByVehicleId(@PathVariable UUID vehicleId) {
        var maintenance = maintenanceService.getMaintenanceByVehicleId(vehicleId);
        return ResponseEntity.ok(maintenance);
    }

    @GetMapping("/status")
    public ResponseEntity<List<MaintenanceViewModel>> getAllByStatus(@RequestParam MaintenanceStatus status){
        var maintenance = maintenanceService.getMaintenanceByStatus(status);
        return ResponseEntity.ok(maintenance);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<MaintenanceViewModel> findById(@PathVariable UUID id) throws Exception{
            var maintenance = maintenanceService.getById(id);
            return ResponseEntity.ok(maintenance);
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> create(@ModelAttribute InsertMaintenance maintenance) throws Exception{
            maintenanceService.createMaintenance(maintenance);
            return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/{id}",consumes = "multipart/form-data")
    public ResponseEntity<?> update(@PathVariable UUID id, @ModelAttribute UpdateMaintenance maintenance) throws Exception{
            maintenanceService.updateMaintenance(id,maintenance);
            return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) throws Exception{
            maintenanceService.deleteMaintenance(id);
            return ResponseEntity.ok().build();
    }

    @PostMapping("/assign")
    public ResponseEntity<?> assign(@RequestBody AssignMaintenanceToVehicle maintenance) throws Exception{
            maintenanceService.assignMaintenanceToVehicle(maintenance.getMaintenanceId(),maintenance.getVehicleId());
            return ResponseEntity.ok().build();
    }

    @DeleteMapping("/unassign")
    public ResponseEntity<?> unAssign(@RequestParam UUID maintenanceId) throws Exception{
        maintenanceService.unAssignMaintenanceToVehicle(maintenanceId);
        return ResponseEntity.ok().build();
    }




}
