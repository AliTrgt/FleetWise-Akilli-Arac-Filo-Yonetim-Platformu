package com.example.VehicleService.controller;

import com.example.VehicleService.dto.driverclient.AssignDriverToVehicle;
import com.example.VehicleService.dto.vehicle.request.InsertVehicle;
import com.example.VehicleService.dto.vehicle.request.UpdateVehicle;
import com.example.VehicleService.dto.vehicle.response.VehicleViewModel;
import com.example.VehicleService.entity.VehicleStatus;
import com.example.VehicleService.service.IVehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/vehicle")
@RequiredArgsConstructor
public class VehicleController {

    private final IVehicleService vehicleService;

    @GetMapping
    public ResponseEntity<?> getAll(){
        List<VehicleViewModel> vehicleViewModels =  vehicleService.getAll();
        return ResponseEntity.ok(vehicleService);
    }

    @PostMapping
    public ResponseEntity<?> insert(
            @RequestHeader("Authorization")String authHeader,@RequestBody InsertVehicle vehicle) throws Exception{
            vehicleService.insert(authHeader,vehicle);
            return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id,@RequestBody UpdateVehicle vehicle) throws Exception{
            vehicleService.update(id,vehicle);
            return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) throws Exception{
            vehicleService.delete(id);
            return ResponseEntity.ok().build();
    }

    @PostMapping("/assign")
    public ResponseEntity<?> assignDriverToVehicle(@RequestBody AssignDriverToVehicle assign)throws Exception{
            vehicleService.assignDriverToVehicle(assign.getDriverId(),assign.getVehicleId());
            return ResponseEntity.ok().build();
    }

    @DeleteMapping("/unAssign/{id}")
    public ResponseEntity<?> unAssignDriverToVehicle(@PathVariable UUID id) throws Exception{
            vehicleService.unAssignDriverToVehicle(id);
            return ResponseEntity.ok().build();
    }

    @GetMapping("/driver/{id}")
    public ResponseEntity<?> findByDriverId(@PathVariable UUID id){
            var vehicles =  vehicleService.findByDriverId(id);
            return ResponseEntity.ok(vehicles);
    }

    @GetMapping("/brand")
    public ResponseEntity<?> findByBrand(@RequestParam String brand){
            var branD = vehicleService.findByBrand(brand);
            return ResponseEntity.ok(branD);
    }

    @GetMapping("/status")
    public ResponseEntity<?> findByStatus(@RequestParam VehicleStatus status){
            var vehicleStatus = vehicleService.findByStatus(status);
            return ResponseEntity.ok(vehicleStatus);
    }

    @GetMapping("/plate")
    public ResponseEntity<?> findByPlateNumber(@RequestParam String plateNumber){
            var plate = vehicleService.findByPlateNumber(plateNumber);
            return ResponseEntity.ok(plate);
    }

}
