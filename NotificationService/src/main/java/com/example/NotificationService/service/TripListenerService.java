package com.example.NotificationService.service;

import com.example.NotificationService.dto.TripDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TripListenerService {

    private final ObjectMapper objectMapper;

    public TripListenerService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "trip-created", groupId = "notification-group")
    public void createdTrip(String message) throws Exception {
        try {
            TripDto tripDto = objectMapper.readValue(message, TripDto.class);
        }catch (Exception e){
                throw new Exception(e);
        }
    }

    @KafkaListener(topics = "trip-start", groupId = "notification-group")
    public void startTrip(String message) throws Exception {
        try {
            TripDto tripDto = objectMapper.readValue(message, TripDto.class);
        }catch (Exception e){
            throw new Exception(e);
        }
    }


    @KafkaListener(topics = "trip-paused", groupId = "notification-group")
    public void pauseTrip(String message) throws Exception {
        try {
            TripDto tripDto = objectMapper.readValue(message, TripDto.class);
        }catch (Exception e){
            throw new Exception(e);
        }
    }

    @KafkaListener(topics = "trip-resume", groupId = "notification-group")
    public void resumeTrip(String message) throws Exception {
        try {
            TripDto tripDto = objectMapper.readValue(message, TripDto.class);
        }catch (Exception e){
            throw new Exception(e);
        }
    }

    @KafkaListener(topics = "trip-cancel", groupId = "notification-group")
    public void cancelTrip(String message) throws Exception {
        try {
            TripDto tripDto = objectMapper.readValue(message, TripDto.class);
        }catch (Exception e){
            throw new Exception(e);
        }
    }

    @KafkaListener(topics = "trip-complete", groupId = "notification-group")
    public void completeTrip(String message) throws Exception {
        try {
            TripDto tripDto = objectMapper.readValue(message, TripDto.class);
        }catch (Exception e){
            throw new Exception(e);
        }
    }


}
