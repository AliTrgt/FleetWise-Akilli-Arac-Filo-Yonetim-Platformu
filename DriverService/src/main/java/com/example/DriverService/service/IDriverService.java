package com.example.DriverService.service;

import com.example.DriverService.dto.driver.request.InsertDriver;
import com.example.DriverService.dto.driver.request.UpdateDriver;
import com.example.DriverService.dto.driver.response.DriverViewModel;

import java.util.List;
import java.util.UUID;

public interface IDriverService {

    List<DriverViewModel> getAll();

    void createDriver(String authHeader, InsertDriver driver) throws Exception;

    void updateDriver(UUID id, UpdateDriver driver) throws Exception;

    void delete(UUID id) throws Exception;

    DriverViewModel getCurrentDriver(String autHeader) throws Exception;

    void updateSalaryByDrivingScore(UUID driverId) throws Exception;

    void updateDriverScore(UUID driverId) throws Exception;

    void sendLicenseExpiryWarning(UUID driverId) throws Exception;

    void suspendDriver(UUID driverId) throws Exception;

}
