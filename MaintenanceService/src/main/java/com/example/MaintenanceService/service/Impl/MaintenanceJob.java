package com.example.MaintenanceService.service.Impl;

import com.example.MaintenanceService.entity.Maintenance;
import com.example.MaintenanceService.repository.IMaintenanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaintenanceJob {

    private final Logger _logger = LoggerFactory.getLogger(MaintenanceJob.class);
    private final IMaintenanceRepository maintenanceRepository;
    private final MaintenanceServiceImpl maintenanceService;

    public MaintenanceJob(IMaintenanceRepository maintenanceRepository, MaintenanceServiceImpl maintenanceService) {
        this.maintenanceRepository = maintenanceRepository;
        this.maintenanceService = maintenanceService;
    }

    @Async
    @Scheduled(cron = "0 0 12 * * ?")
    public void scheduleMaintenance() throws Exception {
        try {
            _logger.info("Starting scheduled maintenance overdue check");
            List<Maintenance> maintenances = maintenanceRepository.findAll();
            for (Maintenance maintenance : maintenances) {
                maintenanceService.isVehicleOverDue(maintenance.getId());
                maintenanceService.markAsCompleted(maintenance.getId());
            }
            _logger.info("Completed scheduled maintenance overdue check for {} maintenance records", maintenances.size());
        } catch (Exception e) {
            _logger.error("Error during scheduled maintenance check: {}", e.getMessage(), e);
            throw e;
        }
    }
}
