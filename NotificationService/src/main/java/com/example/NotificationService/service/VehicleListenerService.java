package com.example.NotificationService.service;

import com.example.NotificationService.dto.VehicleDto;
import com.example.NotificationService.job.MailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class VehicleListenerService {
    private static final Logger _logger = LoggerFactory.getLogger(VehicleListenerService.class);

    private final ObjectMapper objectMapper;
    private final MailService mailService;

    public VehicleListenerService(ObjectMapper objectMapper, MailService mailService) {
        this.objectMapper = objectMapper;
        this.mailService = mailService;
    }

    @KafkaListener(topics = "assign-driver-vehicle", groupId = "notification-group")
    public void assignedDriverToVehicle(String message) throws Exception {
        try {
            VehicleDto vehicleDto = objectMapper.readValue(message, VehicleDto.class);
        }catch (Exception e){
                throw new Exception(e);
        }
    }

    @KafkaListener(topics = "unAssign-driver-vehicle", groupId = "notification-group")
    public void unAssignedDriverToVehicle(String message) throws Exception {
        try {
            VehicleDto vehicleDto = objectMapper.readValue(message, VehicleDto.class);
            System.out.println(vehicleDto);
        }catch (Exception e){
            throw new Exception(e);
        }
    }


}
