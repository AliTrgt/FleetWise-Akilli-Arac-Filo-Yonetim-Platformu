package com.example.DriverService.controller;

import com.example.DriverService.dto.driver.request.InsertDriver;
import com.example.DriverService.dto.driver.request.UpdateDriver;
import com.example.DriverService.dto.driver.response.DriverViewModel;
import com.example.DriverService.service.IDriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/driver")
@RequiredArgsConstructor
public class DriverController {

    private final IDriverService driverService;

    @GetMapping
    public ResponseEntity<List<DriverViewModel>> getAll() {
        List<DriverViewModel> drivers = driverService.getAll();
        return ResponseEntity.ok(drivers);
    }

    @PostMapping
    public ResponseEntity<?> createDriver(
            @RequestHeader("Authorization") String token,
            @RequestBody InsertDriver insertDriver) throws Exception {

        driverService.createDriver(token, insertDriver);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDriver(
            @PathVariable UUID id,
            @RequestBody UpdateDriver updateDriver) throws Exception {

        driverService.updateDriver(id, updateDriver);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) throws Exception {
        driverService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentDriver(
            @RequestHeader("Authorization") String authHeader) throws Exception {
        return ResponseEntity.ok(driverService.getCurrentDriver(authHeader));
    }

    @PutMapping("/salary/{id}")
    public ResponseEntity<?> updateSalaryByUserScore(@PathVariable UUID id) throws Exception {
        driverService.updateSalaryByDrivingScore(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/score/{id}")
    public ResponseEntity<?> updateDriverScore(@PathVariable UUID id) throws Exception {
        driverService.updateDriverScore(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/license/{id}")
    public ResponseEntity<?> sendLicenseExpiryWarning(@PathVariable UUID id) throws Exception {
        driverService.sendLicenseExpiryWarning(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/suspend/{id}")
    public ResponseEntity<?> suspendDriver(@PathVariable UUID id) throws Exception {
        driverService.suspendDriver(id);
        return ResponseEntity.ok().build();

    }


}
