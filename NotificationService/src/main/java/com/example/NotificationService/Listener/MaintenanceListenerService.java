package com.example.NotificationService.Listener;

import com.example.NotificationService.dto.VehicleDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class MaintenanceListenerService {

    private final ObjectMapper objectMapper;

    public MaintenanceListenerService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "maintenance-overdue",groupId = "notification-group")
    public void isVehicleOverDue(String message) throws Exception {
            try {
                VehicleDto vehicleDto = objectMapper.readValue(message,VehicleDto.class);
            }catch (Exception e){
                    throw new Exception(e);
            }
    }

    @KafkaListener(topics = "assigned-maintenance",groupId = "notification-group")
    public void assignMaintenanceToVehicle(String message) throws Exception {
        try {
            VehicleDto vehicleDto = objectMapper.readValue(message,VehicleDto.class);
        }catch (Exception e){
            throw new Exception(e);
        }
    }

    @KafkaListener(topics = "unAssigned-maintenance",groupId = "notification-group")
    public void unAssignMaintenanceToVehicle(String message) throws Exception {
        try {
            VehicleDto vehicleDto = objectMapper.readValue(message,VehicleDto.class);
        }catch (Exception e){
            throw new Exception(e);
        }
    }

    @KafkaListener(topics = "maintenance-completed",groupId = "notification-group")
    public void markAsCompleted(String message) throws Exception {
        try {
            VehicleDto vehicleDto = objectMapper.readValue(message,VehicleDto.class);
        }catch (Exception e){
            throw new Exception(e);
        }
    }



}
