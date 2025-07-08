package com.example.DriverService.service.Impl;

import com.example.DriverService.entity.Driver;
import com.example.DriverService.repository.IDriverRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverJobImpl {
    private static final Logger _logger = LoggerFactory.getLogger(DriverJobImpl.class);
    private final DriverServiceImpl driverService;
    private final IDriverRepository driverRepository;

    public DriverJobImpl(DriverServiceImpl driverService, IDriverRepository driverRepository) {
        this.driverService = driverService;
        this.driverRepository = driverRepository;
    }

    @Scheduled(cron = "0 0 12 ? * MON")
    @Async
    public void scheduleAllDriverInfo() throws Exception {
        try {
            List<Driver> drivers = driverRepository.findAll();
            for (Driver lastDriver : drivers) {
                _logger.info("Starting scheduled operations for driver: {}", lastDriver.getId());

                driverService.updateSalaryByDrivingScore(lastDriver.getId());
                driverService.suspendDriver(lastDriver.getId());
                driverService.updateDriverScore(lastDriver.getId());
                driverService.sendLicenseExpiryWarning(lastDriver.getId());

                _logger.info("Completed scheduled operations for driver: {}", lastDriver.getId());

            }
        } catch (Exception e) {
            _logger.error("An error occurred while processing driver {}",e.getMessage(), e);
            throw new Exception(e);
        }

    }
}
