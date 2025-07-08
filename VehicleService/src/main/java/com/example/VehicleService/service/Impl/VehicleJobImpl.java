package com.example.VehicleService.service.Impl;

import com.example.VehicleService.entity.Vehicle;
import com.example.VehicleService.entity.VehicleStatus;
import com.example.VehicleService.repository.IVehicleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleJobImpl {
    private static final Logger _logger = LoggerFactory.getLogger(VehicleJobImpl.class);
    private final IVehicleRepository vehicleRepository;

    public VehicleJobImpl(IVehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Async
    @Scheduled(cron = "0 0 12 ? * MON")
    public void changeVehicleStatus(){
        List<Vehicle> vehicles = vehicleRepository.findAll();
        for (Vehicle vehicle:vehicles){
               VehicleStatus currentStatus = vehicle.getStatus();
               switch (currentStatus){
                   case OUT_OF_SERVICE -> vehicle.setStatus(VehicleStatus.UNDER_MAINTENANCE);
                   case UNDER_MAINTENANCE -> vehicle.setStatus(VehicleStatus.ACTIVE);
                   default -> {
                        // do nothing
                   }
               }

        }
    }


}
