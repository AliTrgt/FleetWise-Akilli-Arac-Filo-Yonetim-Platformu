package com.example.NotificationService.service;

import com.example.NotificationService.dto.DriverDto;
import com.example.NotificationService.job.MailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class DriverListenerService {

    private final ObjectMapper objectMapper;

    public DriverListenerService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "driver-created",groupId = "notification-group")
    public void listenDriverCreated(String message) throws Exception {
        try {
            DriverDto driverDto = objectMapper.readValue(message, DriverDto.class);
        }catch (Exception e){
            throw new Exception(e);
        }
    }

    @KafkaListener(topics = "license-expiry-warning",groupId = "notification-group")
    public void getLicenseExpiryWarning(String message) throws Exception {
        try {
            DriverDto driverDto = objectMapper.readValue(message, DriverDto.class);

        }catch (Exception e){
            throw new Exception(e);

        }
    }

    @KafkaListener(topics = "suspended-driver",groupId = "notification-group")
    public void getSuspendedDriver(String message) throws Exception {
        try {
            DriverDto driverDto = objectMapper.readValue(message, DriverDto.class);

        }catch (Exception e){
            throw new Exception(e);
        }
    }

}
