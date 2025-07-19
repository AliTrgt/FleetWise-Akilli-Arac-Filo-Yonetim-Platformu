package com.example.NotificationService.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class DriverListenerService {

    @KafkaListener(topics = "driver-created",groupId = "notification-group")
    public void listenDriverCreated(String message){
            // driver dönüyor
    }

    @KafkaListener(topics = "license-expiry-warning",groupId = "notification-group")
    public void getLicenseExpiryWarning(String message){
         //driver dönüyor
    }

    @KafkaListener(topics = "suspended-driver",groupId = "notification-group")
    public void getSuspendedDriver(String message){
            //driver dönüyor
    }

}
